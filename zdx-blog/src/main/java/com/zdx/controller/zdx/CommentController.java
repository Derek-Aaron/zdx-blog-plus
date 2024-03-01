package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Comment;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.CommentPageVo;
import com.zdx.model.vo.front.CommentHomePageVo;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.model.vo.front.ReplyVo;
import com.zdx.service.zdx.CommentService;
import com.zdx.strategy.context.StrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "评论管理")
public class CommentController {

    private final StrategyContext strategyContext;

    private final CommentService commentService;


    @GetMapping("/home/zdx.comment/page")
    @Operation(summary = "前台查询分页评论")
    public Result<IPage<CommentHomePageVo>> homeCommentPage(RequestParams params){
        return Result.success(commentService.homeCommentPage(params));
    }

    @GetMapping("/home/zdx.comment/replyPage")
    @Operation(summary = "前台分页查询回复评论")
    public Result<IPage<ReplyVo>> replyPage(RequestParams params) {
        return Result.success(commentService.replyPage(params));
    }

    @GetMapping("/home/zdx.comment/recent")
    @Operation(summary = "前台查询最近评论")
    public Result<List<RecentCommentVo>> homeRecentComment(){
        return Result.success(commentService.homeRecentComment());
    }

    @GetMapping("/zdx.comment/page")
    @Operation(summary = "分页查询评论")
    public Result<IPage<CommentPageVo>> adminPage(RequestParams params) {
        return Result.success(commentService.adminPage(params));
    }

    @GetMapping("/zdx.comment/like/{id}")
    @Operation(summary = "点赞评论")
    public Result<String> likeComment(@PathVariable @NotBlank String id) {
        strategyContext.executeLike(LikeTypeEnum.COMMENT, id);
        return Result.success();
    }

    @PostMapping("/zdx.comment/add")
    @Operation(summary = "添加评论")
    @Log(type = LogEventEnum.SAVE, desc = "添加评论")
    public Result<String> addComment(@RequestBody @Validated Comment comment) {
        return commentService.addComment(comment) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.comment/through")
    @Operation(summary = "批量审核通过评论")
    @Log(type = LogEventEnum.OTHER, desc = "批量审核通过评论")
    @PreAuthorize("hasAuthority('zdx:comment:examine')")
    public Result<String> through(@RequestBody @NotEmpty @Parameter(description = "评论id") List<String> ids) {
        return commentService.through(ids) ?  Result.success() : Result.error() ;
    }

    @PostMapping("/zdx.comment/batchDelete")
    @Operation(summary = "批量删除评论")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除评论")
    @PreAuthorize("hasAuthority('zdx:comment:delete')")
    public Result<String> batchDelete(@RequestBody @NotEmpty @Parameter(description = "评论id") List<String> ids) {
        return commentService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
