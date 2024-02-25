package com.zdx.controller.tk;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.model.dto.RequestParams;
import com.zdx.entity.tk.Dict;
import com.zdx.handle.Result;
import com.zdx.service.tk.DictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/zdx.dict")
@Api(tags = "数据字典管理")
public class DictController extends BaseController<Dict> {

    private final DictService dictService;
    @Override
    public IService<Dict> getService() {
        return dictService;
    }

    @Override
    public Wrapper<Dict> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Dict::getName, params.getParam("name"));
        queryWrapper.like(params.hasParam("key"), Dict::getKey, params.getParam("key"));
        queryWrapper.eq(params.hasParam("type"), Dict::getType, params.getParam("type"));
        queryWrapper.orderByDesc(Dict::getCreateTime);
        return queryWrapper;
    }


    @GetMapping("/key/{key}")
    @ApiOperation("获取数据字典")
    public Result<Object> dict(@PathVariable @NotBlank String key) {
        return Result.success(dictService.getDictByKey(key));
    }


    @Override
    @PostMapping("/save")
    @ApiOperation("保存字典")
    @PreAuthorize("hasAnyAuthority('zdx:dict:add','zdx:dict:save','zdx:dict:disable')")
    public Result<String> save(@RequestBody @Validated Dict data) {
        return dictService.saveDict(data) ? Result.success() : Result.error();
    }
}
