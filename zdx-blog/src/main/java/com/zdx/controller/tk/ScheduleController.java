package com.zdx.controller.tk;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.annotation.Log;
import com.zdx.controller.BaseController;
import com.zdx.model.dto.RequestParams;
import com.zdx.entity.tk.Schedule;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.service.tk.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/zdx.schedule")
@Validated
@Api(tags = "定时任务管理")
@RequiredArgsConstructor
public class ScheduleController extends BaseController<Schedule> {

    private final ScheduleService scheduleService;
    @Override
    public IService<Schedule> getService() {
        return scheduleService;
    }

    @Override
    public Wrapper<Schedule> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Schedule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Schedule::getName, params.getParam("name"));
        queryWrapper.like(params.hasParam("group"), Schedule::getGroup, params.getParam("group"));
        queryWrapper.eq(params.hasParam("status"), Schedule::getStatus, params.getParam("status", Boolean.class));
        return queryWrapper;
    }


    @GetMapping("/run/{id}")
    @ApiOperation("执行任务")
    public Result<String> run(@PathVariable @NotBlank String id) {
        scheduleService.run(Long.valueOf(id));
        return Result.success();
    }


    @Override
    @PostMapping("/save")
    @Log(type = LogEventEnum.SAVE, desc = "保存任务")
    @ApiOperation("保存任务")
    public Result<String> save(@RequestBody @Validated Schedule data) {
        return scheduleService.saveSchedule(data) ? Result.success() : Result.error();
    }


    @Override
    @Log(type = LogEventEnum.DELETE, desc = "批量删除任务")
    @PostMapping("/batchDelete")
    @ApiOperation("批量删除任务")
    public Result<String> batchDelete(@RequestBody @NotEmpty List<String> ids) {
        return scheduleService.batchDelete(ids) ? Result.success() : Result.error();
    }

    @Override
    @Log(type = LogEventEnum.DELETE, desc = "删除任务")
    @GetMapping("/delete/{id}")
    @ApiOperation("删除任务")
    public Result<String> delete(@PathVariable @NotBlank String id) {
        return scheduleService.deleteSchedule(Long.parseLong(id)) ? Result.success() : Result.error();
    }
}
