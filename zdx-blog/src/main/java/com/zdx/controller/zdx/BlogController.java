package com.zdx.controller.zdx;

import com.zdx.handle.Result;
import com.zdx.model.vo.front.BlogInfoVO;
import com.zdx.model.vo.front.SiteConfig;
import com.zdx.model.vo.front.UserInfoVo;
import com.zdx.service.zdx.BlogService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "博客信息管理")
public class BlogController {

    private final BlogService blogService;
    @GetMapping("/home/report")
    public Result<String> result() {
        blogService.result();
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result<UserInfoVo> userInfo() {
        return Result.success(blogService.getUserInfo());
    }

    @GetMapping("/home/")
    public Result<BlogInfoVO> blogInfo() {
        return Result.success(blogService.getBlogInfo());
    }

    @PostMapping("/zdx.site/save")
    public Result<String> save(@RequestBody @Validated SiteConfig siteConfig) {
        return blogService.saveSiteConfig(siteConfig) ? Result.success() : Result.error();
    }

    @GetMapping("/zdx.site/getSite")
    public Result<SiteConfig> getSite() {
        return Result.success(blogService.getSite());
    }
}
