package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Message;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "留言管理")
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/zdx.message/page")
    @ApiOperation("分页查询留言数据")
    public Result<IPage<Message>> adminPage(RequestParams params) {
        return Result.success(messageService.adminPage(params));
    }
    @PostMapping("/zdx.message/save")
    @ApiOperation("保存留言数据")
    public Result<String> save(@RequestBody @Validated Message friend) {
        return messageService.saveOrUpdate(friend) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.message/batchDelete")
    @ApiOperation("批量删除留言数据")
    public Result<String> batchDelete(@RequestBody @ApiParam("分类id") @NotEmpty List<String> ids) {
        return messageService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.message/through")
    public Result<String> through(@RequestBody @ApiParam("分类id") @NotEmpty List<String> ids) {
        return messageService.through(ids) ? Result.success() : Result.error();
    }
}
