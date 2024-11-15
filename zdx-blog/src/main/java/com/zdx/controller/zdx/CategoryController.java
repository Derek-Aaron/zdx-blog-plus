package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Category;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryCountVo;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.service.zdx.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequiredArgsConstructor
@Tag(name = "分类管理")
@Validated
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/home/zdx.category/list")
    @Operation(summary = "前台查询分类文章数量列表")
    public Result<List<CategoryCountVo>> homeList() {
        return Result.success(categoryService.homeList());
    }

    @GetMapping("/home/zdx.category/articlePage")
    @Operation(summary = "前台分类查询文章分页")
    public Result<IPage<CategoryTagArticleVo>> homeArticlePage(RequestParams params) {
        return Result.success(categoryService.homeArticlePage(params));
    }
    @GetMapping("/zdx.category/page")
    @Operation(summary = "分页查询分类数据")
    public Result<IPage<Category>> adminPage(RequestParams params) {
        return Result.success(categoryService.adminPage(params));
    }
    @GetMapping("/zdx.category/list")
    @Operation(summary = "查询全部分类数据")
    public Result<List<Category>> adminList() {
        return Result.success(categoryService.list());
    }

    @PostMapping("/zdx.category/save")
    @Operation(summary = "保存分类数据")
    @Log(type = LogEventEnum.SAVE, desc = "保存分类数据")
    @PreAuthorize("hasAnyAuthority('zdx:category:add','zdx:category:save')")
    public Result<String> save(@RequestBody @Validated Category category) {
        return categoryService.saveOrUpdate(category) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.category/batchDelete")
    @Operation(summary = "批量删除分类")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除分类")
    @PreAuthorize("hasAuthority('zdx:category:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "分类id") @NotEmpty List<String> ids) {
        return categoryService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
