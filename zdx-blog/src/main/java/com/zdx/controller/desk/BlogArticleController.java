package com.zdx.controller.desk;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.controller.vo.ArticleInfoVo;
import com.zdx.entity.zdx.Article;
import com.zdx.handle.Result;
import com.zdx.service.zdx.ArticleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/desk/zdx.article")
@Api(tags = "博客前台博客接口")
@Validated
@RequiredArgsConstructor
public class BlogArticleController extends BaseController<Article> {

    private final ArticleService articleService;
    @Override
    public IService<Article> getService() {
        return articleService;
    }

    @Override
    public Wrapper<Article> getQueryWrapper(RequestParams params) {
        return new LambdaQueryWrapper<>();
    }

    @Override
    @GetMapping("/page")
    public Result<IPage<Article>> page(RequestParams params) {
        return Result.success(articleService.pageArticle(params, getQueryWrapper(params)));
    }



    @GetMapping("/getHomeById/{id}")
    public Result<ArticleInfoVo> getHomeById(@PathVariable @NotBlank String id) {
        return Result.success(articleService.getHomeArticleById(id));
    }

    @GetMapping("/addView/{id}")
    public Result<?> addView(@PathVariable @NotBlank String id) {
        return articleService.addView(id) ? Result.success() : Result.error();
    }
}
