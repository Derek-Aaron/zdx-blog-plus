package com.zdx.controller.us;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.us.Auth;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.AuthVo;
import com.zdx.service.us.AuthService;
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
@Validated
@Tag(name = "登录项管理")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @GetMapping("/home/zdx.auth/list")
    @Operation(summary = "前台查询登录项列表")
    public Result<List<AuthVo>> homeList() {
        return Result.success(authService.list(new LambdaQueryWrapper<Auth>()
                .eq(Auth::getIsEnabled, Boolean.FALSE)
                .orderByAsc(Auth::getCreateTime)
        ).stream().map(auth -> BeanUtil.copyProperties(auth, AuthVo.class)).toList());
    }
    @GetMapping("/zdx.auth/page")
    @Operation(summary = "后台分页查询登录项")
    public Result<IPage<Auth>> adminPage(RequestParams params) {
        return  Result.success(authService.adminPage(params));
    }

    @PostMapping("/zdx.auth/save")
    @Operation(summary = "保存登录项")
    @Log(type = LogEventEnum.SAVE, desc = "保存登录项")
    @PreAuthorize("hasAnyAuthority('zdx:auth:add', 'zdx:auth:save', 'zdx:auth:disable')")
    public Result<String> save(@RequestBody @Validated Auth auth) {
        return authService.saveOrUpdate(auth) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.auth/batchDelete")
    @Operation(summary = "批量删除登录项")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除登录项")
    @PreAuthorize("hasAuthority('zdx:auth:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(name = "ids", description = "删除id") @NotEmpty List<String> ids) {
        return authService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
