package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.CommentPageVo;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.service.zdx.CommentService;
import io.swagger.annotations.Api;
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
@Api(tags = "评论管理")
public class CommentController {

    private final CommentService commentService;


    @GetMapping("/zdx.comment/page")
    public Result<IPage<CommentPageVo>> adminPage(RequestParams params) {
        return Result.success(commentService.adminPage(params));
    }
    @GetMapping("/home/zdx.comment/recent")
    public Result<List<RecentCommentVo>> homeRecentComment(){
        return Result.success(commentService.homeRecentComment());
    }

    @PostMapping("/zdx.comment/through")
    public Result<String> through(@RequestBody @NotEmpty @ApiParam("评论id") List<String> ids) {
        return commentService.through(ids) ?  Result.success() : Result.error() ;
    }

    @PostMapping("/zdx.comment/batchDelete")
    public Result<String> batchDelete(@RequestBody @NotEmpty @ApiParam("评论id") List<String> ids) {
        return commentService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
