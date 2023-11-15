package com.zdx.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.zdx.annotation.Encrypt;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RegisterDto;
import com.zdx.model.dto.UserLogin;
import com.zdx.model.vo.Router;
import com.zdx.model.vo.UserInfo;
import com.zdx.model.vo.front.AuthRenderVo;
import com.zdx.security.UserSessionFactory;
import com.zdx.security.service.LoginService;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.tk.EmailService;
import com.zdx.service.tk.FileService;
import com.zdx.service.us.AuthService;
import com.zdx.service.us.UserService;
import com.zdx.strategy.AuthStrategy;
import com.zdx.strategy.context.StrategyContext;
import com.zdx.utils.MessageUtil;
import com.zdx.utils.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "登录模块")
public class LoginController {

    private final LoginService loginService;

    private final FileService fileService;

    private final StrategyContext strategyContext;

    private final AuthService authService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "登录")
    @Encrypt
    public Result<Map<String, String>> login(@RequestBody @Validated @Encrypt UserLogin userLogin, HttpServletRequest request) {
        String token = loginService.login(request, userLogin);
        return Result.success(Map.of("token", token));
    }

    @GetMapping("/home/email/code/{email}")
    @ApiOperation("通过邮箱发送验证码")
    public Result<String> emailCode(@PathVariable @Email String email) {
        loginService.sendCode(email);
        return Result.success();
    }

    @PostMapping("/home/register")
    @Encrypt
    @ApiOperation("注册用户")
    public Result<Map<String, String>> register(@RequestBody @Validated @Encrypt RegisterDto register, HttpServletRequest request) {
        if (!emailService.checkCode(register.getEmail(), register.getCode())) {
            return Result.error(MessageUtil.getLocaleMessage("zdx.email.check.error"));
        }
        String password = register.getPassword();
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        if (userService.register(register)) {
            UserLogin userLogin = new UserLogin();
            userLogin.setUsername(register.getUsername());
            userLogin.setPassword(password);
            String token = loginService.login(request, userLogin);
            return Result.success(Map.of("token", token));
        }
        return Result.error();
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



    @GetMapping("/router")
    @ApiOperation("获取路由信息")
    public Result<List<Tree<Router>>> router() {
        List<Router> routers = loginService.routers();
        return Result.success(TreeUtil.createTree(routers, "order"));
    }

    @GetMapping("/oauth/render/{source}/{type}")
    public Result<AuthRenderVo> renderAuth(@PathVariable @NotBlank String source, @PathVariable @NotBlank String type) throws IOException {
        Auth auth = authService.getAuthBySourceAndType(source, type);
        AuthRequest authRequest = strategyContext.executeAuth(AuthSourceEnum.valueOf(source), auth);
        if (ObjUtil.isNull(authRequest)) {
            return Result.error(MessageUtil.getLocaleMessage("zdx.auth.error"));
        }
        String uuid = AuthStateUtils.createState();
        String authorize = authRequest.authorize(uuid);
        AuthStrategy.AUTH_REQUEST_MAP.put(uuid, authRequest);
        AuthRenderVo authRenderVo = new AuthRenderVo();
        authRenderVo.setUuid(uuid);
        authRenderVo.setUrl(authorize);
        return Result.success(authRenderVo);
    }

    @GetMapping("/oauth/callback/{type}")
    public Result<Map<String, String>> login(AuthCallback callback, HttpServletRequest request, @PathVariable @NotBlank String type) throws IOException {
        AuthRequest authRequest = AuthStrategy.AUTH_REQUEST_MAP.get(callback.getState());
        if (ObjUtil.isNull(authRequest)) {
            return Result.error();
        }
        AuthResponse authResponse = authRequest.login(callback);
        if (authResponse.getCode() == 2000) {
            if (authResponse.getData() instanceof AuthUser authUser) {
                String token = loginService.authLogin(authUser, type, request);
                Map<String, String> map = Maps.newHashMap();
                map.put("token", token);
                AuthStrategy.AUTH_REQUEST_MAP.remove(callback.getState());
                return Result.success(map);
            }
        }
        return Result.error();
    }
}
