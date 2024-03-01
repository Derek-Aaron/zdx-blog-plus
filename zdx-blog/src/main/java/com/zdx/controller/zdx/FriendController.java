package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Friend;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.FriendService;
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

@RequiredArgsConstructor
@RestController
@Validated
@Tag(name = "友链管理")
public class FriendController {

    private final FriendService friendService;

    @GetMapping("/home/zdx.friend/list")
    @Operation(summary = "前台查询友链列表")
    public Result<List<Friend>> homeList() {
        return Result.success(friendService.list(new LambdaQueryWrapper<Friend>().orderByAsc(Friend::getCreateTime)));
    }
    @GetMapping("/zdx.friend/page")
    @Operation(summary = "分页查询友链数据")
    public Result<IPage<Friend>> adminPage(RequestParams params) {
        return Result.success(friendService.adminPage(params));
    }
    @PostMapping("/zdx.friend/save")
    @Operation(summary = "保存友链数据")
    @Log(type = LogEventEnum.SAVE, desc = "保存友链数据")
    @PreAuthorize("hasAnyAuthority('zdx:friend:add', 'zdx:friend:delete')")
    public Result<String> save(@RequestBody @Validated Friend friend) {
        return friendService.saveOrUpdate(friend) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.friend/batchDelete")
    @Operation(summary = "批量删除友链数据")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除友链数据")
    @PreAuthorize("hasAuthority('zdx:friend:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "分类id") @NotEmpty List<String> ids) {
        return friendService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
