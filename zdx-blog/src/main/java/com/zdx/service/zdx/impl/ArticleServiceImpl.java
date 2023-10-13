package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zdx.entity.us.Role;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.ArticleContent;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import com.zdx.enums.FileTypeEnum;
import com.zdx.event.EventObject;
import com.zdx.exception.BaseException;
import com.zdx.mapper.zdx.ArticleContentMapper;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.ArticleAdminVo;
import com.zdx.model.vo.ArticleEsVo;
import com.zdx.model.vo.ArticleSaveVo;
import com.zdx.model.vo.front.*;
import com.zdx.search.SearchTemplate;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.tk.FileService;
import com.zdx.service.zdx.ArticleService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

    private final FileService fileService;

    @Autowired(required = false)
    private SearchTemplate searchTemplate;

    @Value("${zdx.es.index}")
    private String esIndex;


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
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().eq(Category::getName, articleSave.getCategoryName()));
        if (ObjUtil.isNull(category)) {
            category = new Category();
            category.setName(articleSave.getCategoryName());
            categoryMapper.insert(category);
        }
        article.setCategoryId(category.getId());
        if (saveOrUpdate(article)) {
            if (ObjUtil.isNotNull(articleSave.getId())) {
                article = getById(article.getId());
            }
            String content = articleSave.getContent();
            ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().select(ArticleContent::getId).eq(ArticleContent::getArticleId, article.getId()));
            if (ObjUtil.isNull(articleContent)) {
                articleContent = new ArticleContent();
            }
            articleContent.setContent(content);
            articleContent.setArticleId(article.getId());
            int count = ObjUtil.isNotNull(articleContent.getId()) ? articleContentMapper.updateById(articleContent) : articleContentMapper.insert(articleContent);
            if (count > 0) {
                List<Tag> tags = saveOrUpdateTag(articleSave.getTagNames(), article.getId());
                //异步同步es服务器
                ArticleEsVo articleEsVo = BeanUtil.copyProperties(article, ArticleEsVo.class);
                articleEsVo.setCategory(category);
                articleEsVo.setContent(articleContent.getContent());
                articleEsVo.setTagVOList(tags);
                applicationContext.publishEvent(new EventObject(articleEsVo, EventObject.Attribute.ES_CREATE_UPDATE));
                return true;
            }
        }
        return false;
    }

    private List<Tag> saveOrUpdateTag(List<String> tagNames, Long articleId) {
        List<Tag> tags = new ArrayList<>();
        List<Long> tagIds = new ArrayList<>();
        if (ObjUtil.isNotNull(articleId) && ObjUtil.isNotNull(tagNames)) {
            for (String tagName : tagNames) {
                Tag tag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getName, tagName));
                if (ObjUtil.isNull(tag)) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tagMapper.insert(tag);
                }
                tags.add(tag);
                Long count = tagMapper.selectCountTagIdAndArticleId(articleId, tag.getId());
                if (!(ObjUtil.isNotNull(count) && count > 0)) {
                    tagIds.add(tag.getId());
                }
            }
        }
        if (!tagIds.isEmpty()) {
            tagMapper.insertTagAndArticleId(articleId, tagIds);
        }
        return tags;
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
        boolean flag = update(updateWrapper);
        if (flag && ObjUtil.isNotNull(searchTemplate)) {
            BulkRequest request = new BulkRequest();
            for (String id : ids) {
                request.add(new DeleteRequest().index(esIndex).id(id));
            }
            return searchTemplate.bulkDoc(esIndex, request);
        }
        return flag;
    }

    @Override
    public boolean batchRecover(List<String> ids) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Article::getTrash, Boolean.FALSE);
        updateWrapper.in(Article::getId, ids);
        boolean flag = update(updateWrapper);
        if (flag) {
            return syncArticle(ids);
        }
        return false;
    }

    @Override
    public IPage<ArticleHomeVo> homePage(RequestParams params) {
        if (ObjUtil.isNotNull(searchTemplate)) {
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchAllQuery());
            FieldSortBuilder createTime = new FieldSortBuilder("createTime").order(SortOrder.DESC);
            return searchTemplate.searchDoc(esIndex, builder, params.getLimit(), params.getPage(), new SortBuilder[]{createTime}, null, ArticleHomeVo.class);
        }
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Article> page = page(iPage, new LambdaQueryWrapper<Article>().orderByDesc(Article::getCreateTime));
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
        if (ObjUtil.isNotNull(searchTemplate)) {
            ArticleHomeInfoVo homeInfoVo = searchTemplate.getDocById(esIndex, id, ArticleHomeInfoVo.class);
            EventObject event = new EventObject(homeInfoVo.getId(), EventObject.Attribute.INSERT_VIEW_COUNT);
            event.setAttribute(EventObject.Attribute.VIEW_COUNT, Optional.ofNullable(homeInfoVo.getViewCount()).orElse(0L));
            applicationContext.publishEvent(event);
            ArticlePaginationVO lastArticle = baseMapper.selectLastArticle(homeInfoVo.getId());
            homeInfoVo.setLastArticle(lastArticle);
            ArticlePaginationVO nextArticle = baseMapper.selectNextArticle(homeInfoVo.getId());
            homeInfoVo.setNextArticle(nextArticle);
            return homeInfoVo;
        }
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
        if (ObjUtil.isNotNull(searchTemplate)) {
            SearchSourceBuilder builder = new SearchSourceBuilder();
            builder.query(QueryBuilders.matchAllQuery());
            FieldSortBuilder createTime = new FieldSortBuilder("createTime").order(SortOrder.DESC);
            return searchTemplate.searchDoc(esIndex, builder, params.getLimit(), params.getPage(), new SortBuilder[]{createTime}, null, ArticleArchivesVo.class);
        }
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getTitle, Article::getCreateTime, Article::getCover);
        queryWrapper.orderByDesc(Article::getCreateTime);
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
        queryWrapper.eq(Article::getTrash, Boolean.FALSE);
        queryWrapper.orderByDesc(Article::getCreateTime);
        queryWrapper.last(" limit 5");
        List<Article> articles = list(queryWrapper);
        return articles.stream().map(article -> BeanUtil.copyProperties(article, ArticleRecommendVo.class)).toList();
    }

    @Override
    public List<ArticleSearchVo> searchArticle(String keyword) {
        if (ObjUtil.isNotNull(searchTemplate)) {
            SearchSourceBuilder builder = new SearchSourceBuilder();
            Result parse = TokenizerUtil.createEngine().parse(keyword);
            List<String> wordsList = Lists.newArrayList();
            while (parse.hasNext()) {
                Word next = parse.next();
                wordsList.add(next.getText());
                parse.remove();
            }
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.should(QueryBuilders.termsQuery("content", wordsList));
            boolQuery.should(QueryBuilders.termsQuery("description", wordsList));
            boolQuery.should(QueryBuilders.termsQuery("title", wordsList));
            builder.query(boolQuery);
            return searchTemplate.searchQuery(esIndex, builder, ArticleSearchVo.class, "title", "description");
        }
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Article::getTitle, keyword);
        queryWrapper.or();
        queryWrapper.like(Article::getDescription, keyword);
        List<ArticleContent> articleContents = articleContentMapper.selectList(new LambdaQueryWrapper<ArticleContent>().like(ArticleContent::getContent, keyword));
        List<Long> articleIds = articleContents.stream().map(ArticleContent::getArticleId).toList();
        queryWrapper.or();
        queryWrapper.in(Article::getId, articleIds);
        return list(queryWrapper).stream().map(article -> BeanUtil.copyProperties(article, ArticleSearchVo.class)).toList();
    }

    @Override
    public boolean syncArticle(List<String> ids) {
        List<Article> articles = listByIds(ids);
        List<ArticleEsVo> articleEsVos = new ArrayList<>();
        for (Article article : articles) {
            ArticleEsVo articleEsVo = BeanUtil.copyProperties(article, ArticleEsVo.class);
            articleEsVo.setCategory(categoryMapper.selectById(article.getCategoryId()));
            articleEsVo.setTagVOList(tagMapper.getTagByArticleId(article.getId()));
            ArticleContent articleContent = articleContentMapper.selectOne(new LambdaQueryWrapper<ArticleContent>().eq(ArticleContent::getArticleId, article.getId()));
            if (ObjUtil.isNotNull(articleContent)) {
                articleEsVo.setContent(articleContent.getContent());
            }
            articleEsVos.add(articleEsVo);
        }
        if (ObjUtil.isNotNull(searchTemplate)) {
            BulkRequest request = new BulkRequest();
            for (ArticleEsVo articleEsVo : articleEsVos) {
                String id = String.valueOf(articleEsVo.getId());
                if (searchTemplate.existsDocById(esIndex, id)) {
                    request.add(new UpdateRequest().index(esIndex).id(id).doc(JSON.toJSONString(articleEsVo), XContentType.JSON));
                } else {
                    request.add(new IndexRequest().index(esIndex).id(id).source(JSON.toJSONString(articleEsVo), XContentType.JSON));
                }
            }
            return searchTemplate.bulkDoc(esIndex, request);
        }
        return false;
    }

    @Override
    public String articleUpload(MultipartFile[] files, String content) throws IOException {
        if (ObjUtil.isNull(files)) {
            throw new BaseException("zdx.operate.error");
        }
        for (MultipartFile file : files) {
            Map<String, String> map = fileService.saveFile(file, FileTypeEnum.ARTICLE.name());
            String[] split = content.split("!\\[");
            if (ObjUtil.isNotNull(split)) {
                for (String str : split) {
                    if (str.contains(Objects.requireNonNull(file.getOriginalFilename()))) {
                        String substring = str.substring(0, str.lastIndexOf(file.getOriginalFilename()));
                        String oldStr = "![" + substring + file.getOriginalFilename() + ")";
                        String newStr = "![](" + map.get("fileUrl") + ")";
                        content = content.replace(oldStr, newStr);
                        break;
                    }
                }
            }
        }
        return content;
    }
}




