package com.zdx.controller.zdx;

import com.zdx.annotation.Log;
import com.zdx.enums.LogEventEnum;
import com.zdx.enums.MusicTypeEnum;
import com.zdx.handle.Result;
import com.zdx.model.vo.BlogBackInfoVO;
import com.zdx.model.vo.front.BlogInfoVO;
import com.zdx.model.vo.front.MusicVo;
import com.zdx.model.vo.front.SiteConfig;
import com.zdx.model.vo.front.UserInfoVo;
import com.zdx.service.zdx.BlogService;
import com.zdx.strategy.context.StrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "博客信息管理")
public class BlogController {

    private final BlogService blogService;

    private final StrategyContext strategyContext;
    @GetMapping("/home/report")
    @Operation(summary = "记录博客访问")
    public Result<String> result() {
        blogService.result();
        return Result.success();
    }

    @GetMapping("/home/music")
    @Operation(summary = "获取音乐")
    public Result<List<MusicVo>> music(String id) {
        return Result.success(strategyContext.executeMusic(MusicTypeEnum.QQ, id));
    }

    @GetMapping("/user/info")
    @Operation(summary = "查询前台用户信息")
    public Result<UserInfoVo> userInfo() {
        return Result.success(blogService.getUserInfo());
    }

    @GetMapping("/home/")
    @Operation(summary = "获取博客信息")
    public Result<BlogInfoVO> blogInfo() {
        return Result.success(blogService.getBlogInfo());
    }

    @GetMapping("/zdx.blog/info")
    @Operation(summary = "查询博客配置信息")
    public Result<BlogBackInfoVO> adminBlogInfo() {
        return Result.success(blogService.adminBlogInfo());
    }

    @PostMapping("/zdx.site/save")
    @Operation(summary = "保存博客配置信息")
    @Log(type = LogEventEnum.SAVE, desc = "保存博客配置信息")
    @PreAuthorize("hasAuthority('zdx:site:save')")
    public Result<String> save(@RequestBody @Validated SiteConfig siteConfig) {
        return blogService.saveSiteConfig(siteConfig) ? Result.success() : Result.error();
    }

    @GetMapping("/zdx.site/getSite")
    @Operation(summary = "获取博客配置信息")
    public Result<SiteConfig> getSite() {
        return Result.success(blogService.getSite());
    }
}
