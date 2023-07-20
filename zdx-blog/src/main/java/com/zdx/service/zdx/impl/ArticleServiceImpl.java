package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Role;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.ArticleContent;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import com.zdx.event.EventObject;
import com.zdx.mapper.zdx.ArticleContentMapper;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.ArticleAdminVo;
import com.zdx.model.vo.ArticleSaveVo;
import com.zdx.model.vo.front.*;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.tk.RedisService;
import com.zdx.service.zdx.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author zhaodengxuan
 * @description 针对表【zdx_article】的数据库操作Service实现
 * @createDate 2023-07-17 16:51:40
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    private final TagMapper tagMapper;

    private final CategoryMapper categoryMapper;

    private final ArticleContentMapper articleContentMapper;

    private final ApplicationContext applicationContext;

    private final RedisService redisService;

    @Override
    public IPage<ArticleAdminVo> adminPage(RequestParams params) {
        IPage<Article> articleIPage = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("keyword"), Article::getTitle, params.getParam("keyword"));
        queryWrapper.eq(params.hasParam("categoryId"), Article::getCategoryId, params.getParam("categoryId"));
        queryWrapper.eq(params.hasParam("type"), Article::getType, params.getParam("type"));
        if (params.hasParam("tagId")) {
            List<String> articleId = tagMapper.getArticleIdByTagId(params.getParam("tagId", String.class));
            queryWrapper.in(Article::getId, articleId);
        }
        if (!UserSessionFactory.hasRole(Role.ADMIN_ROLE_ID)) {
            queryWrapper.eq(Article::getUserId, UserSessionFactory.getUserId());
        }
        queryWrapper.eq(params.hasParam("trash"), Article::getTrash, params.getParam("trash", Boolean.class));
        queryWrapper.eq(params.hasParam("status"), Article::getStatus, params.getParam("status"));
        IPage<Article> page = page(articleIPage, queryWrapper);
        List<ArticleAdminVo> articleAdminVos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            ArticleAdminVo articleAdminVo = BeanUtil.copyProperties(article, ArticleAdminVo.class);
            Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getName).eq(Category::getId, article.getCategoryId()));
            if (ObjUtil.isNotNull(category)) {
                articleAdminVo.setCategoryName(category.getName());
            }
            articleAdminVos.add(articleAdminVo);
        }
        IPage<ArticleAdminVo> articleAdminVoIPage = new Page<>(params.getPage(), params.getLimit());
        articleAdminVoIPage.setTotal(page.getTotal());
        articleAdminVoIPage.setPages(page.getPages());
        articleAdminVoIPage.setSize(page.getSize());
        articleAdminVoIPage.setCurrent(page.getCurrent());
        articleAdminVoIPage.setRecords(articleAdminVos);
        return articleAdminVoIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean adminSave(ArticleSaveVo articleSave) {
        Article article = BeanUtil.copyProperties(articleSave, Article.class);
        article.setUserId(UserSessionFactory.getUserId());
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getName).eq(Category::getName, articleSave.getCategoryName()));
        if (ObjUtil.isNull(category)) {
            category = new Category();
            category.setName(articleSave.getCategoryName());
            categoryMapper.insert(category);
        }
        article.setCategoryId(category.getId());
        if (saveOrUpdate(article)) {
            String content = articleSave.getContent();
            ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().select(ArticleContent::getId).eq(ArticleContent::getArticleId, article.getId()));
            if (ObjUtil.isNull(articleContent)) {
                articleContent = new ArticleContent();
            }
            articleContent.setContent(content);
            articleContent.setArticleId(article.getId());
            int count = ObjUtil.isNotNull(articleContent.getId()) ? articleContentMapper.updateById(articleContent) : articleContentMapper.insert(articleContent);
            if (count > 0) {
                //扩展文章多端同步
                EventObject event = new EventObject(articleSave.getTagNames(), EventObject.Attribute.SAVEORUPDATETAGS);
                event.setAttribute(EventObject.Attribute.ARTICLE_ID, article.getId());
                applicationContext.publishEvent(event);
                return true;
            }
        }
        return false;
    }

    @Override
    public ArticleSaveVo adminGetById(String id) {
        Article article = getById(id);
        ArticleSaveVo articleSaveVo = null;
        if (ObjUtil.isNotNull(article)) {
            articleSaveVo = BeanUtil.copyProperties(article, ArticleSaveVo.class);
            ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().select(ArticleContent::getContent).eq(ArticleContent::getArticleId, id));
            if (ObjUtil.isNotNull(articleContent)) {
                articleSaveVo.setContent(articleContent.getContent());
            }
            Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getName).eq(Category::getId, article.getCategoryId()));
            if (ObjUtil.isNotNull(category)) {
                articleSaveVo.setCategoryName(category.getName());
            }
            List<Tag> tags = tagMapper.getTagByArticleId(article.getId());
            if (ObjUtil.isNotNull(tags) && !tags.isEmpty()) {
                articleSaveVo.setTagNames(tags.stream().map(Tag::getName).toList());
            }
        }
        return articleSaveVo;
    }

    @Override
    public boolean batchTrash(List<String> ids) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getTrash, Boolean.TRUE);
        updateWrapper.in(Article::getId, ids);
        return update(updateWrapper);
    }

    @Override
    public boolean batchRecover(List<String> ids) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getTrash, Boolean.FALSE);
        updateWrapper.in(Article::getId, ids);
        return update(updateWrapper);
    }

    @Override
    public IPage<ArticleHomeVo> homePage(RequestParams params) {
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Article> page = page(iPage);
        IPage<ArticleHomeVo> articleHomeVoIPage = new Page<>(params.getPage(), params.getLimit());
        List<ArticleHomeVo> articleHomeVos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            ArticleHomeVo articleHomeVo = BeanUtil.copyProperties(article, ArticleHomeVo.class);
            articleHomeVo.setTagVOList(tagMapper.getTagByArticleId(article.getId()));
            articleHomeVo.setCategory(categoryMapper.selectById(article.getCategoryId()));
            articleHomeVos.add(articleHomeVo);
        }
        articleHomeVoIPage.setPages(page.getPages());
        articleHomeVoIPage.setCurrent(page.getCurrent());
        articleHomeVoIPage.setSize(page.getSize());
        articleHomeVoIPage.setTotal(page.getTotal());
        articleHomeVoIPage.setRecords(articleHomeVos);
        return articleHomeVoIPage;
    }

    @Override
    public ArticleHomeInfoVo getHomeById(String id) {
        Article article = getById(id);
        if (ObjUtil.isNull(article)) {
            return null;
        }

        EventObject event = new EventObject(article.getId(), EventObject.Attribute.INSERT_VIEW_COUNT);
        event.setAttribute(EventObject.Attribute.VIEW_COUNT, Optional.ofNullable(article.getViewCount()).orElse(0L));
        applicationContext.publishEvent(event);
        ArticleHomeInfoVo articleHomeInfoVo = BeanUtil.copyProperties(article, ArticleHomeInfoVo.class);
        ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().eq(ArticleContent::getArticleId, id));
        if (ObjUtil.isNotNull(articleContent)) {
            articleHomeInfoVo.setContent(articleContent.getContent());
        }
        Category category = categoryMapper.selectById(article.getCategoryId());
        articleHomeInfoVo.setCategory(category);
        articleHomeInfoVo.setTagVOList(tagMapper.getTagByArticleId(article.getId()));
        ArticlePaginationVO lastArticle = baseMapper.selectLastArticle(article.getId());
        articleHomeInfoVo.setLastArticle(lastArticle);
        ArticlePaginationVO nextArticle = baseMapper.selectNextArticle(article.getId());
        articleHomeInfoVo.setNextArticle(nextArticle);
        return articleHomeInfoVo;
    }

    @Override
    public IPage<ArticleArchivesVo> archivesPage(RequestParams params) {
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateTime, Article::getCover);
        IPage<Article> page = page(iPage, queryWrapper);
        List<ArticleArchivesVo> articleArchivesVos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            ArticleArchivesVo articleArchivesVo = BeanUtil.copyProperties(article, ArticleArchivesVo.class);
            articleArchivesVos.add(articleArchivesVo);
        }
        IPage<ArticleArchivesVo> articleArchivesVoIPage = new Page<>(params.getPage(), params.getLimit());
        articleArchivesVoIPage.setPages(page.getPages());
        articleArchivesVoIPage.setTotal(page.getTotal());
        articleArchivesVoIPage.setSize(page.getSize());
        articleArchivesVoIPage.setCurrent(page.getCurrent());
        articleArchivesVoIPage.setRecords(articleArchivesVos);
        return articleArchivesVoIPage;
    }

    @Override
    public List<ArticleRecommendVo> homeRecommend() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateTime, Article::getCover);
        queryWrapper.last(" limit 5");
        List<Article> articles = list(queryWrapper);
        return articles.stream().map(article -> BeanUtil.copyProperties(article, ArticleRecommendVo.class)).toList();
    }
}




