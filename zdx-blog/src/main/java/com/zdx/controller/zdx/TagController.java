package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Tag;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.TagCountVo;
import com.zdx.service.zdx.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@io.swagger.v3.oas.annotations.tags.Tag(name = "标签管理")
public class TagController {



    private final TagService tagService;

    @GetMapping("/home/zdx.tag/list")
    @Operation(summary = "前台查询标签列表")
    public Result<List<TagCountVo>> homeList() {
        return Result.success(tagService.homeList());
    }

    @GetMapping("/home/zdx.tag/articlePage")
    @Operation(summary = "前台分页查询标签文章")
    public Result<IPage<CategoryTagArticleVo>> homeArticlePage(RequestParams params) {
        return Result.success(tagService.homeArticlePage(params));
    }

    @GetMapping("/zdx.tag/page")
    @Operation(summary = "分页查询标签")
    public Result<IPage<Tag>> adminPage(RequestParams params) {
        return Result.success(tagService.adminTag(params));
    }

    @GetMapping("/zdx.tag/list")
    @Operation(summary = "查询全部标签")
    public Result<List<Tag>> adminList() {
        return Result.success(tagService.list());
    }

    @PostMapping("/zdx.tag/save")
    @Operation(summary = "保存标签数据")
    @Log(type = LogEventEnum.SAVE, desc = "保存标签数据")
    @PreAuthorize("hasAnyAuthority('zdx:tag:add', 'zdx:tag:save')")
    public Result<String> save(@RequestBody @Validated Tag tag) {
        return tagService.saveOrUpdate(tag) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.tag/batchDelete")
    @Operation(summary = "批量删除标签")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除标签")
    @PreAuthorize("hasAuthority('zdx:tag:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "标签id") @NotEmpty List<String> ids) {
        return tagService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
