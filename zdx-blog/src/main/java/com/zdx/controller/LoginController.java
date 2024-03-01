package com.zdx.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.zdx.annotation.Encrypt;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RegisterDto;
import com.zdx.model.dto.UserLogin;
import com.zdx.model.vo.AuthRequestVo;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "登录模块")
@Slf4j
public class LoginController {

    private final LoginService loginService;

    private final FileService fileService;

    private final StrategyContext strategyContext;

    private final AuthService authService;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    @Encrypt
    public Result<Map<String, String>> login(@RequestBody @Validated @Encrypt UserLogin userLogin, HttpServletRequest request) {
        String token = loginService.login(request, userLogin);
        return Result.success(Map.of("token", token));
    }

    @GetMapping("/home/email/code/{email}")
    @Operation(summary = "通过邮箱发送验证码")
    public Result<String> emailCode(@PathVariable @Email String email) {
        loginService.sendCode(email);
        return Result.success();
    }

    @PostMapping("/home/register")
    @Encrypt
    @Operation(summary = "注册用户")
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
    @Operation(summary = "后台登录用户信息")
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
    @Operation(summary = "获取路由信息")
    public Result<List<Tree<Router>>> router() {
        List<Router> routers = loginService.routers();
        return Result.success(TreeUtil.createTree(routers, "order"));
    }

    @GetMapping("/oauth/render/{source}")
    @Operation(summary = "单点登录")
    public Result<AuthRenderVo> renderAuth(@PathVariable @NotBlank String source, HttpServletRequest request) {
        Auth auth = authService.getAuthBySourceAndType(source);
        AuthRequest authRequest = strategyContext.executeAuth(AuthSourceEnum.valueOf(source), auth);
        if (ObjUtil.isNull(authRequest)) {
            return Result.error(MessageUtil.getLocaleMessage("zdx.auth.error"));
        }
        String uuid = AuthStateUtils.createState();
        String authorize = authRequest.authorize(uuid);
        AuthRequestVo authRequestVo = new AuthRequestVo();
        authRequestVo.setState(uuid);
        authRequestVo.setAuthRequest(authRequest);
        authRequestVo.setAttribute("address", request.getServerName());
        AuthStrategy.AUTH_REQUEST_MAP.put(uuid, authRequestVo);
        AuthRenderVo authRenderVo = new AuthRenderVo();
        authRenderVo.setUuid(uuid);
        authRenderVo.setUrl(authorize);
        return Result.success(authRenderVo);
    }

    @GetMapping("/oauth/callback")
    @Operation(summary = "单点登录回调")
    public void login(AuthCallback callback, HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthRequestVo authRequestVo = AuthStrategy.AUTH_REQUEST_MAP.get(callback.getState());
        if (ObjUtil.isNull(authRequestVo)) {
            return;
        }
        AuthResponse<?> authResponse = authRequestVo.getAuthRequest().login(callback);
        if (authResponse.getCode() == 2000) {
            if (authResponse.getData() instanceof AuthUser authUser) {
                String token = loginService.authLogin(authUser, request);
                AuthStrategy.AUTH_REQUEST_MAP.remove(callback.getState());
                response.sendRedirect("https://" + authRequestVo.getAttributes().get("address") + "/?token=" + token);
            }
        }
    }
}
