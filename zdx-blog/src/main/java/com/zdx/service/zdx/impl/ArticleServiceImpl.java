package com.zdx.service.zdx.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.ArticleContent;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import com.zdx.mapper.zdx.ArticleContentMapper;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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




