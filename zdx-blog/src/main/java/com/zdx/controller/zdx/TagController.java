package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Tag;
import com.zdx.service.zdx.TagService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.tag")
@RequiredArgsConstructor
@Api(tags = "标签管理")
@Validated
public class TagController extends BaseController<Tag> {

    private final TagService tagService;
    @Override
    public IService<Tag> getService() {
        return tagService;
    }

    @Override
    public Wrapper<Tag> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Tag::getName, params.hasParam("name"));
        return queryWrapper;
    }
}
