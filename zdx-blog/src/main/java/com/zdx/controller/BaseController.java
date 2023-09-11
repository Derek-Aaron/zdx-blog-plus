package com.zdx.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.annotation.Log;
import com.zdx.exception.BaseException;
import com.zdx.model.dto.RequestParams;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.strategy.context.StrategyContext;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

@Validated
public abstract class BaseController<T>{


    public abstract IService<T> getService();


    public abstract Wrapper<T> getQueryWrapper(RequestParams params);

    @Autowired
    private StrategyContext strategyContext;

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<IPage<T>> page(@Validated RequestParams params) {
        IPage<T> page = new Page<>(params.getPage(), params.getLimit());
        return Result.success(getService().page(page, getQueryWrapper(params)));
    }

    @GetMapping("/list")
    @ApiOperation("查询全部")
    public Result<List<T>> list() {
        return Result.success(getService().list());
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("id获取一个")
    public Result<T> getById(@PathVariable @NotBlank String id) {
        return  Result.success(getService().getById(id));
    }


    @PostMapping("/save")
    @ApiOperation("保存数据")
    @Log(type = LogEventEnum.SAVE, desc = "保存数据")
    @PreAuthorize("hasAnyAuthority('zdx:role:save', 'zdx:role:add','zdx:config:add', 'zdx:config:save')")
    public Result<String> save(@RequestBody @Validated T data) {
        return getService().saveOrUpdate(data)? Result.success() : Result.error();
    }

    @PostMapping("/import")
    @ApiOperation("导入数据")
    @Log(type = LogEventEnum.IMPORT, desc = "导入数据")
    public Result<?> importData(@ApiParam("文件") MultipartFile file, @ApiParam("类型") String type) {
        try {
            strategyContext.importData(file, type, getService());
        } catch (IOException e) {
            return Result.error();
        }
        return Result.success();
    }

    @GetMapping("/export")
    @ApiOperation("导出数据")
    @Log(type = LogEventEnum.SAVE, desc = "导出数据")
    public void exportData(@ApiParam("类型") String type, HttpServletResponse response) {
        try {
            strategyContext.exportData(response, type, new LambdaQueryWrapper<>(), getService());
        } catch (Exception e) {
            throw new BaseException("zdx.export.error");
        }
    }


    @GetMapping("/delete/{id}")
    @ApiOperation("id删除")
    @Log(type = LogEventEnum.DELETE, desc = "删除")
    @PreAuthorize("hasAnyAuthority('zdx:user:delete','zdx:role:delete','zdx:menu:delete','zdx:dict:delete','zdx:config:delete')")
    public Result<String> delete(@PathVariable @NotBlank String id) {
        return getService().removeById(id) ? Result.success() : Result.error();
    }

    @PostMapping("/batchDelete")
    @ApiOperation("id批量删除")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除")
    @PreAuthorize("hasAnyAuthority('zdx:user:delete','zdx:role:delete','zdx:menu:delete','zdx:dict:delete','zdx:config:delete')")
    public Result<String> batchDelete(@RequestBody @NotEmpty List<String> ids) {
        return getService().removeBatchByIds(ids) ? Result.success() : Result.error();
    }

}
