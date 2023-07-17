package com.zdx.controller.desk;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.controller.dto.RequestParams;
import com.zdx.controller.vo.CommentHomeVo;
import com.zdx.handle.Result;
import com.zdx.service.zdx.CommentService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "前台评论接口")
@Validated
@RequestMapping("/desk/zdx.comment")
public class BlogCommentController{


    private final CommentService commentService;
    @GetMapping("/page")
    public Result<IPage<CommentHomeVo>> page(RequestParams params) {
        return Result.success(commentService.pageHomeCommentVo(params));
    }
}
