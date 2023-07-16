package com.zdx.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.StrUtil;
import com.zdx.annotation.Encrypt;
import com.zdx.controller.dto.UserLogin;
import com.zdx.controller.vo.HomeUserInfo;
import com.zdx.controller.vo.Router;
import com.zdx.controller.vo.UserInfo;
import com.zdx.handle.Result;
import com.zdx.security.UserSessionFactory;
import com.zdx.security.service.LoginService;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.tk.FileService;
import com.zdx.service.us.UserService;
import com.zdx.utils.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "登录模块")
public class LoginController {

    private final LoginService loginService;

    private final FileService fileService;

    private final UserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    @Encrypt
    public Result<Map<String, String>> login(@RequestBody @Validated @Encrypt UserLogin userLogin, HttpServletRequest request) {
        String token = loginService.login(request, userLogin);
        return Result.success(Map.of("token", token));
    }


    @GetMapping("/info")
    @ApiOperation("后台登录用户信息")
    public Result<UserInfo> info() {
        UserSession userSession = UserSessionFactory.userDetails();
        UserInfo userInfo = BeanUtil.copyProperties(((UserPrincipal) userSession).getUser(), UserInfo.class);
        if (StrUtil.isNotBlank(userInfo.getAvatar()) && !userInfo.getAvatar().startsWith("http")) {
            String fileUrl = fileService.getFileUrl(userInfo.getAvatar());
            userInfo.setAvatar(fileUrl);
        }
        userInfo.setRoles(userSession.getRoles());
        userInfo.setPermissions(userSession.getPermissions());
        return Result.success(userInfo);
    }

    @GetMapping("/homeInfo")
    public Result<HomeUserInfo> homeInfo() {
        return Result.success(userService.getHomeInfo());
    }



    @GetMapping("/router")
    @ApiOperation("获取路由信息")
    public Result<List<Tree<Router>>> router() {
        List<Router> routers = loginService.routers();
        return Result.success(TreeUtil.createTree(routers, "order"));
    }
}
