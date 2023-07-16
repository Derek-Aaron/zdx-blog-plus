package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.annotation.Log;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.us.Role;
import com.zdx.entity.zdx.Article;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.ArticleService;
import com.zdx.service.zdx.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/zdx.article")
@RequiredArgsConstructor
@Api(tags = "文章管理")
@Validated
public class ArticleController extends BaseController<Article> {

    private final ArticleService articleService;

    private final TagService tagService;
    @Override
    public IService<Article> getService() {
        return articleService;
    }

    @Override
    public Wrapper<Article> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("keyword"), Article::getArticleTitle, params.getParam("keyword"));
        queryWrapper.eq(params.hasParam("categoryId"), Article::getCategoryId, params.getParam("categoryId"));
        queryWrapper.eq(params.hasParam("articleType"), Article::getArticleType, params.getParam("articleType"));
        if (params.hasParam("tagId")) {
            List<String> articleId = tagService.getArticleIdByTagId(params.getParam("tagId", String.class));
            queryWrapper.in(Article::getId, articleId);
        }
        if (!UserSessionFactory.hasRole(Role.ADMIN_ROLE_ID)) {
            queryWrapper.eq(Article::getUserId, UserSessionFactory.getUserId());
        }
        queryWrapper.eq(params.hasParam("trash"), Article::getTrash, params.getParam("trash", Boolean.class));
        return queryWrapper;
    }

    @Override
    @GetMapping("/page")
    @ApiOperation("分页查询博客")
    public Result<IPage<Article>> page(RequestParams params) {
        return Result.success(articleService.pageArticle(params, getQueryWrapper(params)));
    }

    @Override
    @GetMapping("/getById/{id}")
    @ApiOperation("通过id查询文章")
    public Result<Article> getById( @PathVariable @NotBlank String id) {
        return Result.success(articleService.getArticleById(id));
    }

    @PostMapping("/batchTrash")
    @ApiOperation("批量回收文章")
    @Log(type = LogEventEnum.DELETE, desc = "批量回收文章")
    public Result<String> batchTrash(@RequestBody @NotEmpty List<String> ids) {
        return articleService.batchTrash(ids) ? Result.success() : Result.error();
    }


    @Override
    @PostMapping("/save")
    @ApiOperation("保存文章内容")
    public Result<String> save(@RequestBody @Validated Article data) {
        return articleService.saveArticle(data) ? Result.success() : Result.error();
    }
}
