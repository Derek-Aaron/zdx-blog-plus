package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Message;
import com.zdx.handle.Result;
import com.zdx.service.zdx.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@RestController
@RequestMapping("/zdx.message")
@RequiredArgsConstructor
@Api(tags = "留言管理")
@Validated
public class MessageController extends BaseController<Message> {

    private final MessageService messageService;
    @Override
    public IService<Message> getService() {
        return messageService;
    }

    @Override
    public Wrapper<Message> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("nickname"), Message::getNickname, params.getParam("nickname"));
        queryWrapper.eq(params.hasParam("isCheck"), Message::getIsCheck, params.getParam("isCheck", Boolean.class));
        return queryWrapper;
    }

    @PostMapping("/through")
    @ApiOperation("批量审核通过留言")
    public Result<String> through(@RequestBody @NotEmpty List<String> ids) {
        return messageService.through(ids) ? Result.success() : Result.error();
    }
}
