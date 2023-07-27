package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.Category;
import com.zdx.enums.ArticleStatusEnum;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.CategoryCountVo;
import com.zdx.service.zdx.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_category】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    private final ArticleMapper articleMapper;

    private final TagMapper tagMapper;

    @Override
    public IPage<Category> adminPage(RequestParams params) {
        IPage<Category> iPage = new Page<>(params.getPage(), params.getLimit());

        return page(iPage, new LambdaQueryWrapper<Category>()
                .like(params.hasParam("name"), Category::getName, params.getParam("name")));
    }

    @Override
    public List<CategoryCountVo> homeList() {
        return baseMapper.selectCategoryCountVo();
    }

    @Override
    public IPage<CategoryTagArticleVo> homeArticlePage(RequestParams params) {
        IPage<Article> iPage = new Page<>(params.getPage(), params.getLimit());
        //查询文章的查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getId, Article::getCategoryId, Article::getCover, Article::getCreateTime, Article::getTitle);
        queryWrapper.eq(Article::getCategoryId, params.getParam("categoryId"));
        queryWrapper.eq(Article::getTrash, Boolean.FALSE);
        queryWrapper.eq(Article::getStatus, ArticleStatusEnum.PUBLICITY.name());
        IPage<Article> page = articleMapper.selectPage(iPage, queryWrapper);
        //封装vo对象
        List<CategoryTagArticleVo> categoryArticleVos = new ArrayList<>();
        for (Article article : page.getRecords()) {
            CategoryTagArticleVo categoryArticleVo = BeanUtil.copyProperties(article, CategoryTagArticleVo.class);
            categoryArticleVo.setCategory(baseMapper.selectById(article.getCategoryId()));
            categoryArticleVo.setTagVOList(tagMapper.getTagByArticleId(article.getId()));
            categoryArticleVos.add(categoryArticleVo);
        }
        IPage<CategoryTagArticleVo> categoryArticleVoIPage = new Page<>(params.getPage(), params.getLimit());
        categoryArticleVoIPage.setPages(page.getPages());
        categoryArticleVoIPage.setCurrent(page.getCurrent());
        categoryArticleVoIPage.setSize(page.getSize());
        categoryArticleVoIPage.setTotal(page.getTotal());
        categoryArticleVoIPage.setRecords(categoryArticleVos);
        return categoryArticleVoIPage;
    }
}




