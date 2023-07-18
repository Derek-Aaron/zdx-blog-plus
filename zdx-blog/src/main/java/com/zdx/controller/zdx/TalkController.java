package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Talk;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.TalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "说说管理")
@Validated
public class TalkController {


    private final TalkService talkService;

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
