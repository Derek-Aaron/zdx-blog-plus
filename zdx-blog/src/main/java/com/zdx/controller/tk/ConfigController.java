package com.zdx.controller.tk;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.model.dto.RequestParams;
import com.zdx.entity.tk.Config;
import com.zdx.service.tk.ConfigService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.config")
@Validated
@RequiredArgsConstructor
@Api(tags = "配置管理")
public class ConfigController extends BaseController<Config> {

    private final ConfigService configService;
    @Override
    public IService<Config> getService() {
        return configService;
    }

    @Override
    public Wrapper<Config> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Config::getName, params.getParam("name"));
        queryWrapper.eq(params.hasParam("type"), Config::getType, params.getParam("type"));
        return queryWrapper;
    }
}
