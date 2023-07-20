package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Comment;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.CommentPageVo;
import com.zdx.model.vo.front.CommentHomePageVo;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.model.vo.front.ReplyVo;
import com.zdx.service.zdx.CommentService;
import com.zdx.strategy.context.StrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "评论管理")
public class CommentController {

    private final StrategyContext strategyContext;

    private final CommentService commentService;


    @GetMapping("/home/zdx.comment/page")
    public Result<IPage<CommentHomePageVo>> homeCommentPage(RequestParams params){
        return Result.success(commentService.homeCommentPage(params));
    }

    @GetMapping("/home/zdx.comment/replyPage")
    public Result<IPage<ReplyVo>> replyPage(RequestParams params) {
        return Result.success(commentService.replyPage(params));
    }


    @GetMapping("/zdx.comment/page")
    public Result<IPage<CommentPageVo>> adminPage(RequestParams params) {
        return Result.success(commentService.adminPage(params));
    }
    @GetMapping("/home/zdx.comment/recent")
    public Result<List<RecentCommentVo>> homeRecentComment(){
        return Result.success(commentService.homeRecentComment());
    }


    @GetMapping("/zdx.comment/like/{id}")
    public Result<String> likeComment(@PathVariable @NotBlank String id) {
        strategyContext.executeLike(LikeTypeEnum.COMMENT, id);
        return Result.success();
    }

    @PostMapping("/zdx.comment/add")
    public Result<String> addComment(@RequestBody @Validated Comment comment) {
        return commentService.addComment(comment) ? Result.success() : Result.error();
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
