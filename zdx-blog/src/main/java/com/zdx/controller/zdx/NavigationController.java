package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Navigation;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.NavigationVo;
import com.zdx.service.zdx.NavigationService;
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
@Validated
@Tag(name= "导航管理")
public class NavigationController {

    private final NavigationService navigationService;
    @GetMapping("/zdx.navigation/page")
    @Operation(summary = "分页查询导航数据")
    public Result<IPage<Navigation>> page(RequestParams params) {
        return Result.success(navigationService.adminPage(params));
    }


    @GetMapping("/home/zdx.navigation/group")
    @Operation(summary = "分组查询导航")
    public Result<List<NavigationVo>> group() {
        return Result.success(navigationService.homeGroup());
    }

    @PostMapping("/zdx.navigation/save")
    @Log(type = LogEventEnum.SAVE, desc = "保存导航数据")
    @Operation(summary = "保存导航数据")
    @PreAuthorize("hasAnyAuthority('zdx:navigation:add','zdx:navigation:save')")
    public Result<String> save(@RequestBody @Validated Navigation navigation) {
        return navigationService.saveOrUpdate(navigation) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.navigation/batchDelete")
    @Operation(summary = "批量删除导航数据")
    @Log(type = LogEventEnum.SAVE, desc = "批量删除导航数据")
    @PreAuthorize("hasAnyAuthority('zdx:navigation:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "留言id") @NotEmpty List<String> ids) {
        return navigationService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
