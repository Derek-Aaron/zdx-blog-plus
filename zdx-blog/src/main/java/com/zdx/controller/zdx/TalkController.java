package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Talk;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;
import com.zdx.model.vo.front.TalkHomeInfoVo;
import com.zdx.model.vo.front.TalkHomeListVo;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.TalkService;
import com.zdx.strategy.context.StrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    private final StrategyContext strategyContext;

    private final TalkService talkService;

    @GetMapping("/home/zdx.talk/page")
    @ApiOperation("前台分页查询说说")
    public Result<IPage<TalkHomeListVo>> homePage(RequestParams params) {
        return Result.success(talkService.homePage(params));
    }
    @GetMapping("/home/zdx.talk/getById/{id}")
    @ApiOperation("前台通过id查询说说")
    public Result<TalkHomeInfoVo> homeGetById(@PathVariable @NotBlank String id) {
        return Result.success(talkService.homeGetById(id));
    }

    @GetMapping("/home/zdx.talk/list")
    @ApiOperation("前台查询说说列表")
    public Result<List<String>> homeList() {
        return  Result.success(talkService.homeList());
    }

    @GetMapping("/zdx.talk/like/{id}")
    @ApiOperation("点赞说说")
    public Result<String> likeTalk(@PathVariable String id) {
        strategyContext.executeLike(LikeTypeEnum.TALK, id);
        return Result.success();
    }

    @GetMapping("/zdx.talk/page")
    @ApiOperation("后台分页查询说说")
    public Result<IPage<TalkPageVo>> adminPage(RequestParams params) {
        return Result.success(talkService.adminPage(params));
    }

    @PostMapping("/zdx.talk/save")
    @ApiOperation("保存说说")
    @Log(type = LogEventEnum.SAVE, desc = "保存说说")
    public Result<String> save(@RequestBody @Validated Talk talk) {
        talk.setUserId(UserSessionFactory.getUserId());
        return talkService.saveOrUpdate(talk) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.talk/batchDelete")
    @ApiOperation("批量删除说说")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除说说")
    public Result<String> batchDelete(@RequestBody @Validated @ApiParam("说说id") List<String> ids) {
        return talkService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
