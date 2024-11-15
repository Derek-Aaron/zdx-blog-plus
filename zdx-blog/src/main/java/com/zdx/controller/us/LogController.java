package com.zdx.controller.us;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.model.dto.RequestParams;
import com.zdx.entity.us.Log;
import com.zdx.handle.Result;
import com.zdx.service.us.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/zdx.log")
@Validated
@RequiredArgsConstructor
@Tag(name = "日志管理")
public class LogController extends BaseController<Log> {

    private final LogService logService;
    @Override
    public IService<Log> getService() {
        return logService;
    }

    @Override
    public Wrapper<Log> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Log> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(params.hasParam("event"), Log::getEvent, params.getParam("event"));
        queryWrapper.like(params.hasParam("username"), Log::getUsername, params.getParam("username"));
        queryWrapper.orderByDesc(Log::getCreateTime);
        return queryWrapper;
    }

    @GetMapping("/clear")
    @Operation(summary = "清除日志")
    @PreAuthorize("hasAnyAuthority('zdx:log-login:clear', 'zdx:log-operate:clear')")
    public Result<String> clear(String event) {
        return logService.clear(event) ? Result.success() : Result.error();
    }
}
