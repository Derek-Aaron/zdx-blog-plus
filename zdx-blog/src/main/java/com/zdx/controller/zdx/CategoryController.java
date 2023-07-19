package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Category;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.CategoryCountVo;
import com.zdx.service.zdx.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "分类管理")
@Validated
public class CategoryController {

    private final CategoryService categoryService;


    @GetMapping("/home/zdx.category/list")
    public Result<List<CategoryCountVo>> homeList() {
        return Result.success(categoryService.homeList());
    }

    @GetMapping("/home/zdx.category/articlePage")
    public Result<IPage<CategoryTagArticleVo>> homeArticlePage(RequestParams params) {
        return Result.success(categoryService.homeArticlePage(params));
    }
    @GetMapping("/zdx.category/page")
    @ApiOperation("分页查询分类数据")
    public Result<IPage<Category>> adminPage(RequestParams params) {
        return Result.success(categoryService.adminPage(params));
    }
    @GetMapping("/zdx.category/list")
    @ApiOperation("查询全部分类数据")
    public Result<List<Category>> adminList() {
        return Result.success(categoryService.list());
    }

    @PostMapping("/zdx.category/save")
    @ApiOperation("保存分类数据")
    public Result<String> save(@RequestBody @Validated Category category) {
        return categoryService.saveOrUpdate(category) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.category/batchDelete")
    public Result<String> batchDelete(@RequestBody @ApiParam("分类id") @NotEmpty List<String> ids) {
        return categoryService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
