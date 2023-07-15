package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Talk;
import com.zdx.handle.Result;
import com.zdx.service.zdx.TalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.talk")
@RequiredArgsConstructor
@Api(tags = "说说管理")
@Validated
public class TalkController extends BaseController<Talk> {

    private final TalkService talkService;
    @Override
    public IService<Talk> getService() {
        return talkService;
    }

    @Override
    public Wrapper<Talk> getQueryWrapper(RequestParams params) {
        return new LambdaQueryWrapper<>();
    }

    @Override
    @GetMapping("/page")
    @ApiOperation("分页查询说说")
    public Result<IPage<Talk>> page(RequestParams params) {
        return Result.success(talkService.pageTalk(params, getQueryWrapper(params)));
    }
}
