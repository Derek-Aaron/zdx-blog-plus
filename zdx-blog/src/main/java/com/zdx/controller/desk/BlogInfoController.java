package com.zdx.controller.desk;


import com.zdx.controller.vo.BlogInfoVO;
import com.zdx.controller.vo.HomeUserInfo;
import com.zdx.handle.Result;
import com.zdx.service.blog.BlogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "博客信息管理")
@Validated
@RequiredArgsConstructor
@RequestMapping("/desk")
public class BlogInfoController {

    private final BlogInfoService blogInfoService;


    @ApiOperation(value = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.success();
    }

    @GetMapping("/")
    public Result<BlogInfoVO> getBlogInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }

    @GetMapping("/info")
    public Result<HomeUserInfo> getHomeUserInfo() {
        return Result.success( blogInfoService.getHomeUserInfo());
    }
}
