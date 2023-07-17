package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.controller.dto.RequestParams;
import com.zdx.controller.vo.ArticleInfoVo;
import com.zdx.controller.vo.ArticlePageVo;
import com.zdx.controller.vo.ArticlePaginationVO;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.ArticleContent;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import com.zdx.enums.ArticleLikeTypeEnum;
import com.zdx.enums.ArticleStatusEnum;
import com.zdx.mapper.zdx.ArticleContentMapper;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.ArticleService;
import com.zdx.strategy.context.LikeStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Service实现
* @createDate 2023-07-14 17:23:34
*/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ArticleContentMapper articleContentMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    private final LikeStrategyContext strategyContext;

    @Override
    public IPage<ArticlePageVo> pageArticlePageVo(RequestParams params, Wrapper<Article> queryWrapper) {
        LambdaUpdateWrapper<Article> lambdaUpdateWrapper = (LambdaUpdateWrapper<Article>) queryWrapper;
        lambdaUpdateWrapper.eq(Article::getTrash, false);
        lambdaUpdateWrapper.eq(Article::getStatus, ArticleStatusEnum.PUBLICITY.name());
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Article> page = page(iPage, lambdaUpdateWrapper);
        List<ArticlePageVo> articlePageVos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            setArticleVoInfo(article, false);
            ArticlePageVo articlePageVo = BeanUtil.copyProperties(article, ArticlePageVo.class);
            articlePageVos.add(articlePageVo);
        }
        IPage<ArticlePageVo> articlePageVoIPage = new Page<>(params.getPage(), params.getLimit());
        articlePageVoIPage.setTotal(page.getTotal());
        articlePageVoIPage.setRecords(articlePageVos);
        return articlePageVoIPage;
    }

    private void setArticleVoInfo(Article article, Boolean isContent) {
        Category category = categoryMapper.selectById(article.getCategoryId());
        if (ObjUtil.isNotNull(category)) {
            article.setCategory(category);
            article.setCategoryName(category.getName());
        }
        List<Tag> tags = tagMapper.getTagByArticleId(article.getId());
        if (ObjUtil.isNotNull(tags) && !tags.isEmpty()) {
            article.setTagVOList(tags);
            article.setTagNames(tags.stream().map(Tag::getName).toList());
        }
        if (isContent) {
            ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().select(ArticleContent::getContent).eq(ArticleContent::getArticleId, article.getId()));
            if (ObjUtil.isNotNull(articleContent)) {
                article.setContent(articleContent.getContent());
            }
        }
    }

    @Override
    public Article getArticleById(String id) {
        Article article = getById(id);
        setArticleVoInfo(article, true);
        return article;
    }

    @Override
    public boolean addView(String id) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getViewCount, Article::getId);
        queryWrapper.eq(Article::getId, id);
        Article article = getOne(queryWrapper);
        if (ObjUtil.isNotNull(article)) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            Long quantity = article.getViewCount();
            if (ObjUtil.isNull(quantity)) {
                quantity = 1L;
            }
            updateWrapper.set(Article::getViewCount, quantity + 1);
            updateWrapper.eq(Article::getId, id);
            if (update(updateWrapper)) {
//                applicationContext.publishEvent(new EventObject(article, EventObject.Attribute.ES_CREATE_UPDATE));
                return true;
            }
        }
        return false;
    }

    @Override
    public IPage<Article> pageArticle(RequestParams params, Wrapper<Article> queryWrapper) {
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Article> page = page(iPage, queryWrapper);
        List<Article> articles = new ArrayList<>();
        for (Article article : page.getRecords()) {
            setArticleVoInfo(article, false);
            articles.add(article);
        }
        page.setRecords(articles);
        return page;
    }

    @Override
    public ArticleInfoVo getHomeArticleById(String id) {
        Article article = getArticleById(id);
        ArticleInfoVo articleInfoVo = BeanUtil.copyProperties(article, ArticleInfoVo.class);
        ArticlePaginationVO last = baseMapper.selectLastArticle(id);
        ArticlePaginationVO next = baseMapper.selectNextArticle(id);
        articleInfoVo.setLastArticle(last);
        articleInfoVo.setNextArticle(next);
        redisTemplate.opsForZSet().incrementScore(Constants.ARTICLE_VIEW_COUNT, id, 1D);
        Double score = redisTemplate.opsForZSet().score(Constants.ARTICLE_VIEW_COUNT, id);
        Double viewCount = Optional.ofNullable(score)
                .orElse((double) 0);
        articleInfoVo.setViewCount(viewCount.longValue());
        Object o = redisTemplate.opsForHash().get(Constants.ARTICLE_LIKE_COUNT, id);
        if (ObjUtil.isNotNull(o)) {
            articleInfoVo.setLikeCount(Long.valueOf((Integer) Optional.of(o).orElse(0)));
        }
        return articleInfoVo;
    }

    @Override
    public boolean likeArticle(String id) {
        strategyContext.executeLikeStrategy(ArticleLikeTypeEnum.ARTICLE, id);
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchTrash(List<String> ids) {
        for (String id : ids) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Article::getTrash, Boolean.TRUE);
            updateWrapper.eq(Article::getId, id);
            if (!update(updateWrapper)) {
                return  false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveArticle(Article data) {
        data.setUserId(UserSessionFactory.getUserId());
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId).eq(Category::getName, data.getCategoryName()));
        if (ObjUtil.isNull(category)) {
            category = new Category();
            category.setName(data.getCategoryName());
            categoryMapper.insert(category);
        }
        data.setCategoryId(category.getId());
        if (saveOrUpdate(data)) {
            saveOrTags(data.getId(), data.getTagNames());
            ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().select(ArticleContent::getId).eq(ArticleContent::getArticleId, data.getId()));
            if (ObjUtil.isNull(articleContent)) {
                articleContent = new ArticleContent();
            }
            articleContent.setContent(data.getContent());
            articleContent.setArticleId(data.getId());
            int count = ObjUtil.isNotNull(articleContent.getId()) ? articleContentMapper.update(articleContent, new LambdaUpdateWrapper<ArticleContent>().eq(ArticleContent::getId, articleContent.getId())) : articleContentMapper.insert(articleContent);
            return count > 0;
        }
        return false;
    }
    private void saveOrTags(Long id, List<String> tagNames) {
        List<Long> tagIds = tagMapper.selectList(new LambdaQueryWrapper<Tag>().select(Tag::getId).in(Tag::getName, tagNames))
                .stream().map(Tag::getId).toList();
        for (Long tagId : tagIds) {
            int count = tagMapper.getCountByArticleIdAndTagId(id, tagId);
            if (count <= 0) {
                tagMapper.insertArticleTag(id, tagId);
            }
        }
    }
}




