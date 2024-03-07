package com.zdx.controller.zdx;


import cn.hutool.core.codec.Base64Decoder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.ArticleAdminVo;
import com.zdx.model.vo.ArticleSaveVo;
import com.zdx.model.vo.front.*;
import com.zdx.service.zdx.ArticleService;
import com.zdx.strategy.context.StrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "博客管理")
@Validated
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    private final StrategyContext strategyContext;

    @GetMapping("/home/zdx.article/page")
    @Operation(summary = "前台查询文章列表")
    public Result<IPage<ArticleHomeVo>> homePage(RequestParams params) {
        return Result.success(articleService.homePage(params));
    }

    @GetMapping("/home/zdx.article/search")
    @Operation(summary = "前台搜索文章")
    public Result<List<ArticleSearchVo>> searchArticle(String keyword) {
        return Result.success(articleService.searchArticle(keyword));
    }

    @GetMapping("/home/zdx.article/getHomeById/{id}")
    @Operation(summary = "前台查询文章")
    public Result<ArticleHomeInfoVo> getHomeById(@PathVariable @NotBlank String id) {
        return Result.success(articleService.getHomeById(id));
    }

    @GetMapping("/home/zdx.article/recommend")
    @Operation(summary = "前台查询前台推荐文章")
    public Result<List<ArticleRecommendVo>> homeRecommend() {
        return Result.success(articleService.homeRecommend());
    }

    @GetMapping("/home/zdx.article/archives")
    @Operation(summary = "前台归档查询文章")
    public Result<IPage<ArticleArchivesVo>> archivesPage(RequestParams params) {
        return  Result.success(articleService.archivesPage(params));
    }

    @GetMapping("/zdx.article/page")
    @Operation(summary = "后台文章分页")
    public Result<IPage<ArticleAdminVo>> adminPage(RequestParams params) {
        return Result.success(articleService.adminPage(params));
    }
    @GetMapping("/zdx.article/getById/{id}")
    @Operation(summary = "后台通过文章id查询文章")
    @Log(type = LogEventEnum.BROWSE, desc = "博客浏览")
    public Result<ArticleSaveVo> adminGetById(@PathVariable @NotBlank String id) {
        return Result.success(articleService.adminGetById(id));
    }

    @PostMapping("/zdx.article/save")
    @Operation(summary = "保存文章")
    @Log(type = LogEventEnum.SAVE, desc = "保存文章")
    @PreAuthorize("hasAnyAuthority('zdx:article:save','zdx:article:add')")
    public Result<String> save(@RequestBody @Validated ArticleSaveVo articleSave) {
        return articleService.adminSave(articleSave) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/sync")
    @Operation(summary = "批量同步es服务器")
    @PreAuthorize("hasAuthority('zdx:article:es')")
    public Result<String> sync(@RequestBody @Parameter(description = "文章id") @NotEmpty List<String> ids) {
        return articleService.syncArticle(ids) ? Result.success() : Result.error();
    }

    @GetMapping("/zdx.article/likeArticle/{id}")
    @Operation(summary = "点赞文章")
    public Result<String> likeArticle(@PathVariable @NotBlank String id) {
        strategyContext.executeLike(LikeTypeEnum.ARTICLE, id);
        return Result.success();
    }

    @PostMapping("/zdx.article/batchTrash")
    @Operation(summary = "批量回收文章")
    @Log(type = LogEventEnum.DELETE, desc = "批量回收文章")
    @PreAuthorize("hasAuthority('zdx:article:trash')")
    public Result<String> batchTrash(@RequestBody @Parameter(description = "文章id") @NotEmpty List<String> ids) {
        return articleService.batchTrash(ids) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/batchDelete")
    @Operation(summary = "彻底删除文章")
    @Log(type = LogEventEnum.DELETE, desc = "彻底删除文章")
    @PreAuthorize("hasAuthority('zdx:article:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "文章id") @NotEmpty List<String> ids) {
        return articleService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/batchRecover")
    @Operation(summary = "批量恢复文章")
    @Log(type = LogEventEnum.OTHER, desc = "批量恢复文章")
    @PreAuthorize("hasAuthority('zdx:article:recover')")
    public Result<String> batchRecover(@RequestBody @Parameter(description = "文章id") @NotEmpty List<String> ids) {
        return articleService.batchRecover(ids) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.article/upload")
    @Operation(summary = "文章导入")
    @Log(type = LogEventEnum.IMPORT, desc = "文章导入")
    @PreAuthorize("hasAnyAuthority('zdx:article:save','zdx:article:add')")
    public Result<Map<String, String>> articleImport(@Parameter(description = "文件") MultipartFile[] files, @Parameter(description = "文件内容（需使用Base64加密）") String content) throws IOException {
        return Result.success(Map.of("content", articleService.articleUpload(files, Base64Decoder.decodeStr(content))));
    }
}
