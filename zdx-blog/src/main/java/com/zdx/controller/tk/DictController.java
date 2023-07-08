package com.zdx.controller.tk;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.tk.Dict;
import com.zdx.handle.Result;
import com.zdx.service.tk.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;


@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/zdx.dict")
public class DictController extends BaseController<Dict> {

    private final DictService dictService;
    @Override
    public IService<Dict> getService() {
        return dictService;
    }

    @Override
    public Wrapper<Dict> getQueryWrapper(RequestParams params) {
        return null;
    }


    @GetMapping("/key/{key}")
    public Result<Dict> dict(@PathVariable @NotBlank String key) {
        return Result.success(dictService.getDictByKey(key));
    }
}
