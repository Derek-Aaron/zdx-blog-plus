package com.zdx.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.dto.RequestParams;
import com.zdx.handle.Result;
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
    public Result<IPage<T>> page(@Validated RequestParams params) {
        IPage<T> page = new Page<>(params.getPage(), params.getLimit());
        return Result.success(getService().page(page, getQueryWrapper(params)));
    }


    @GetMapping("/getById/{id}")
    public Result<T> getById(@PathVariable @NotBlank String id) {
        return  Result.success(getService().getById(id));
    }


    @PostMapping("/save")
    public Result<String> save(@RequestBody @Validated T data) {
        return getService().saveOrUpdate(data)? Result.success() : Result.error();
    }


    @GetMapping("/delete/{id}")
    public Result<String> delete(@PathVariable @NotBlank String id) {
        return getService().removeById(id) ? Result.success() : Result.error();
    }

    @PostMapping("/batchDelete")
    public Result<String> batchDelete(@RequestBody @NotEmpty List<String> ids) {
        return getService().removeBatchByIds(ids) ? Result.success() : Result.error();
    }

}
