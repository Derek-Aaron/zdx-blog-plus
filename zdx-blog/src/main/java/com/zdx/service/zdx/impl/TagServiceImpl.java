package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.Tag;
import com.zdx.enums.ArticleStatusEnum;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.TagCountVo;
import com.zdx.service.zdx.TagService;
import com.zdx.mapper.zdx.TagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
@RequiredArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    private final ArticleMapper articleMapper;

    private final CategoryMapper categoryMapper;

    @Override
    public void insertTagAndArticleId(Long articleId, List<Long> tagIds) {
        baseMapper.insertTagAndArticleId(articleId, tagIds);
    }

    @Override
    public Long selectCountTagIdAndArticleId(Long articleId, Long tagId) {
        return baseMapper.selectCountTagIdAndArticleId(articleId, tagId);
    }

    @Override
    public IPage<Tag> adminTag(RequestParams params) {
        IPage<Tag> iPage = new Page<>(params.getPage(), params.getLimit());
        return page(iPage, new LambdaQueryWrapper<Tag>()
                .like(params.hasParam("name"), Tag::getName, params.getParam("name")));
    }

    @Override
    public List<TagCountVo> homeList() {
        List<TagCountVo> tagCountVos = new ArrayList<>();
        for (Tag tag : list()) {
            TagCountVo tagCountVo = BeanUtil.copyProperties(tag, TagCountVo.class);
            Long articleCount = baseMapper.selectArticleCountByTagId(tag.getId());
            tagCountVo.setArticleCount(articleCount);
            tagCountVos.add(tagCountVo);
        }
        return tagCountVos;
    }

    @Override
    public IPage<CategoryTagArticleVo> homeArticlePage(RequestParams params) {
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        //查询文章的查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getCategoryId, Article::getCover, Article::getCreateTime, Article::getTitle);
        List<String> articleId = baseMapper.getArticleIdByTagId(params.getParam("tagId", String.class));
        queryWrapper.in(Article::getId, articleId);
        queryWrapper.eq(Article::getTrash, Boolean.FALSE);
        queryWrapper.eq(Article::getStatus, ArticleStatusEnum.PUBLICITY.name());
        IPage<Article> page = articleMapper.selectPage(iPage, queryWrapper);
        List<CategoryTagArticleVo> categoryTagArticleVos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            CategoryTagArticleVo categoryArticleVo = BeanUtil.copyProperties(article, CategoryTagArticleVo.class);
            categoryArticleVo.setCategory(categoryMapper.selectById(article.getCategoryId()));
            categoryArticleVo.setTagVOList(baseMapper.getTagByArticleId(article.getId()));
            categoryTagArticleVos.add(categoryArticleVo);
        }
        IPage<CategoryTagArticleVo> categoryArticleVoIPage = new Page<>(params.getPage(), params.getLimit());
        categoryArticleVoIPage.setPages(page.getPages());
        categoryArticleVoIPage.setCurrent(page.getCurrent());
        categoryArticleVoIPage.setSize(page.getSize());
        categoryArticleVoIPage.setTotal(page.getTotal());
        categoryArticleVoIPage.setRecords(categoryTagArticleVos);
        return categoryArticleVoIPage;
    }
}




