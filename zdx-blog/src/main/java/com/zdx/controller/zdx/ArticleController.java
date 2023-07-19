package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.ArticleAdminVo;
import com.zdx.model.vo.ArticleSaveVo;
import com.zdx.model.vo.front.ArticleArchivesVo;
import com.zdx.model.vo.front.ArticleHomeInfoVo;
import com.zdx.model.vo.front.ArticleHomeVo;
import com.zdx.service.zdx.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@Api(tags = "博客管理")
@Validated
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/zdx.article/page")
    @ApiOperation("后台文章分页")
    public Result<IPage<ArticleAdminVo>> adminPage(RequestParams params) {
        return Result.success(articleService.adminPage(params));
    }

    @GetMapping("/home/zdx.article/page")
    @ApiOperation("前台查询文章列表")
    public Result<IPage<ArticleHomeVo>> homePage(RequestParams params) {
        return Result.success(articleService.homePage(params));
    }

    @GetMapping("/home/zdx.article/getHomeById/{id}")
    @ApiOperation("前台查询文章")
    public Result<ArticleHomeInfoVo> getHomeById(@PathVariable @NotBlank String id) {
        return Result.success(articleService.getHomeById(id));
    }

    @GetMapping("/home/zdx.article/archives")
    public Result<IPage<ArticleArchivesVo>> archivesPage(RequestParams params) {
        return  Result.success(articleService.archivesPage(params));
    }

    @GetMapping("/zdx.article/getById/{id}")
    @ApiOperation("后台通过文章id查询文章")
    public Result<ArticleSaveVo> adminGetById(@PathVariable @NotBlank String id) {
        return Result.success(articleService.adminGetById(id));
    }

    @PostMapping("/zdx.article/save")
    @ApiOperation("保存文章")
    public Result<String> save(@RequestBody @Validated ArticleSaveVo articleSave) {
        return articleService.adminSave(articleSave) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/batchTrash")
    @ApiOperation("批量回收文章")
    public Result<String> batchTrash(@RequestBody @ApiParam("文章id") @NotEmpty List<String> ids) {
        return articleService.batchTrash(ids) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/batchDelete")
    @ApiOperation("彻底删除文章")
    public Result<String> batchDelete(@RequestBody @ApiParam("文章id") @NotEmpty List<String> ids) {
        return articleService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/batchRecover")
    public Result<String> batchRecover(@RequestBody @ApiParam("文章id") @NotEmpty List<String> ids) {
        return articleService.batchRecover(ids) ? Result.success() : Result.error();
    }
}
