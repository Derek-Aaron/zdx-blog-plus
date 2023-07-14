package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Comment;
import com.zdx.handle.Result;
import com.zdx.service.us.UserService;
import com.zdx.service.zdx.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@RestController
@RequestMapping("/zdx.comment")
@RequiredArgsConstructor
@Api(tags = "评论管理")
@Validated
public class CommentController extends BaseController<Comment> {

    private final CommentService commentService;

    private final UserService userService;
    @Override
    public IService<Comment> getService() {
        return commentService;
    }

    @Override
    public Wrapper<Comment> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        if (params.hasParam("nickname")) {
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.select(User::getId);
            userQueryWrapper.like(User::getNickname, params.getParam("nickname"));
            userQueryWrapper.or();
            userQueryWrapper.like(User::getUsername, params.getParam("nickname"));
            List<Long> userIds = userService.list(userQueryWrapper).stream().map(User::getId).toList();
            queryWrapper.in(Comment::getFromUid, userIds);
        }
        queryWrapper.eq(params.hasParam("isCheck"), Comment::getIsCheck, params.getParam("isCheck", Boolean.class));
        queryWrapper.eq(params.hasParam("commentType"), Comment::getCommentType, params.getParam("commentType"));
        return queryWrapper;
    }

    @Override
    @GetMapping("/page")
    @ApiOperation("分页查询评论")
    public Result<IPage<Comment>> page(RequestParams params) {
        return Result.success(commentService.pageCommentVo(params, getQueryWrapper(params)));
    }


    @PostMapping("/through")
    @ApiOperation("批量审核通过评论")
    public Result<String> through(@RequestBody @NotEmpty List<String> ids) {
        return commentService.through(ids) ? Result.success() : Result.error();
    }
}
