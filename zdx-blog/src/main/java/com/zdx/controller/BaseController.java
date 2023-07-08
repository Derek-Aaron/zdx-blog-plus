package com.zdx.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.annotation.Log;
import com.zdx.controller.dto.RequestParams;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
public abstract class BaseController<T> {


    public abstract IService<T> getService();


    public abstract Wrapper<T> getQueryWrapper(RequestParams params);

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
    public Result<String> save(@RequestBody @Validated T data) {
        return getService().saveOrUpdate(data)? Result.success() : Result.error();
    }


    @GetMapping("/delete/{id}")
    @ApiOperation("id删除")
    @Log(type = LogEventEnum.DELETE, desc = "删除")
    public Result<String> delete(@PathVariable @NotBlank String id) {
        return getService().removeById(id) ? Result.success() : Result.error();
    }

    @PostMapping("/batchDelete")
    @ApiOperation("id批量删除")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除")
    public Result<String> batchDelete(@RequestBody @NotEmpty List<String> ids) {
        return getService().removeBatchByIds(ids) ? Result.success() : Result.error();
    }

}
