package com.zdx.controller.zdx;

import com.zdx.annotation.Log;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.vo.front.BlogInfoVO;
import com.zdx.model.vo.front.SiteConfig;
import com.zdx.model.vo.front.UserInfoVo;
import com.zdx.service.zdx.BlogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("记录博客访问")
    public Result<String> result() {
        blogService.result();
        return Result.success();
    }

    @GetMapping("/user/info")
    @ApiOperation("查询前台用户信息")
    public Result<UserInfoVo> userInfo() {
        return Result.success(blogService.getUserInfo());
    }

    @GetMapping("/home/")
    @ApiOperation("获取博客信息")
    public Result<BlogInfoVO> blogInfo() {
        return Result.success(blogService.getBlogInfo());
    }

    @PostMapping("/zdx.site/save")
    @ApiOperation("保存博客配置信息")
    @Log(type = LogEventEnum.SAVE, desc = "保存博客配置信息")
    public Result<String> save(@RequestBody @Validated SiteConfig siteConfig) {
        return blogService.saveSiteConfig(siteConfig) ? Result.success() : Result.error();
    }

    @GetMapping("/zdx.site/getSite")
    @ApiOperation("获取博客配置信息")
    public Result<SiteConfig> getSite() {
        return Result.success(blogService.getSite());
    }
}
