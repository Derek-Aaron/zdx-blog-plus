package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Friend;
import com.zdx.service.zdx.FriendService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.friend")
@RequiredArgsConstructor
@Api(tags = "友链管理")
@Validated
public class FriendController extends BaseController<Friend> {

    private final FriendService friendService;
    @Override
    public IService<Friend> getService() {
        return friendService;
    }

    @Override
    public Wrapper<Friend> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(params.hasParam("name"), Friend::getName, params.getParam("name"));
        return queryWrapper;
    }
}
