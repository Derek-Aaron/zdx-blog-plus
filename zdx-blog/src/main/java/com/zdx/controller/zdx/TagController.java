package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Tag;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.TagCountVo;
import com.zdx.service.zdx.TagService;
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
@Validated
@RequiredArgsConstructor
@Api(tags = "标签管理")
public class TagController {



    private final TagService tagService;

    @GetMapping("/home/zdx.tag/list")
    public Result<List<TagCountVo>> homeList() {
        return Result.success(tagService.homeList());
    }

    @GetMapping("/home/zdx.tag/articlePage")
    public Result<IPage<CategoryTagArticleVo>> homeArticlePage(RequestParams params) {
        return Result.success(tagService.homeArticlePage(params));
    }

    @GetMapping("/zdx.tag/page")
    @ApiOperation("分页查询标签")
    public Result<IPage<Tag>> adminPage(RequestParams params) {
        return Result.success(tagService.adminTag(params));
    }

    @GetMapping("/zdx.tag/list")
    @ApiOperation("查询全部标签")
    public Result<List<Tag>> adminList() {
        return Result.success(tagService.list());
    }

    @PostMapping("/zdx.tag/save")
    @ApiOperation("保存标签数据")
    public Result<String> save(@RequestBody @Validated Tag tag) {
        return tagService.saveOrUpdate(tag) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.tag/batchDelete")
    @ApiOperation("批量删除标签")
    public Result<String> batchDelete(@RequestBody @ApiParam("标签id") @NotEmpty List<String> ids) {
        return tagService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
