package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Talk;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;
import com.zdx.model.vo.front.TalkHomeInfoVo;
import com.zdx.model.vo.front.TalkHomeListVo;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.TalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "说说管理")
@Validated
public class TalkController {


    private final TalkService talkService;

    @GetMapping("/home/zdx.talk/page")
    public Result<IPage<TalkHomeListVo>> homePage(RequestParams params) {
        return Result.success(talkService.homePage(params));
    }
    @GetMapping("/home/zdx.talk/getById/{id}")
    public Result<TalkHomeInfoVo> homeGetById(@PathVariable @NotBlank String id) {
        return Result.success(talkService.homeGetById(id));
    }

    @GetMapping("/home/zdx.talk/list")
    public Result<List<String>> homeList() {
        return  Result.success(talkService.homeList());
    }

    @GetMapping("/zdx.talk/page")
    public Result<IPage<TalkPageVo>> adminPage(RequestParams params) {
        return Result.success(talkService.adminPage(params));
    }

    @PostMapping("/zdx.talk/save")
    public Result<String> save(@RequestBody @Validated Talk talk) {
        talk.setUserId(UserSessionFactory.getUserId());
        return talkService.saveOrUpdate(talk) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.talk/batchDelete")
    public Result<String> batchDelete(@RequestBody @Validated @ApiParam("说说id") List<String> ids) {
        return talkService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
