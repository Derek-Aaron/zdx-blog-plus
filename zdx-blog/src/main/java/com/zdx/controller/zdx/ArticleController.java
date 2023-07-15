package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.annotation.Log;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Article;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.service.zdx.ArticleService;
import com.zdx.service.zdx.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return queryWrapper;
    }


    @PostMapping("/batchTrash")
    @ApiOperation("批量回收文章")
    @Log(type = LogEventEnum.DELETE, desc = "批量回收文章")
    public Result<String> batchTrash(@RequestBody @NotEmpty List<String> ids) {
        return articleService.batchTrash(ids) ? Result.success() : Result.error();
    }
}
