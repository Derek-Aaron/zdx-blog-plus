package com.zdx.security.service;

import com.zdx.controller.vo.Router;
import com.zdx.controller.dto.UserLogin;
import com.zdx.event.EventObject;
import com.zdx.security.vo.UserAgent;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.service.tk.MenuService;
import com.zdx.utils.IpAddressUtil;
import com.zdx.utils.UserAgentUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MenuService menuService;

    /**
     * 登录
     * @param request  请求
     * @param userLogin 请求参数
     * @return token
     */
    public String login(HttpServletRequest request, UserLogin userLogin) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        UserPrincipal userPrincipal = (UserPrincipal) Objects.requireNonNull(authenticate).getPrincipal();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 生成token
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        loginEvent(request, userPrincipal);
        return permissionService.createToken(userPrincipal);
    }


    /**
     * 返回当前登录用户的系统信息
     * @param request 请求
     * @param userPrincipal 用户实体
     * @return 系统信息
     */
    private UserAgent getUserAgent(HttpServletRequest request, UserPrincipal userPrincipal) {
        UserAgent userAgent = new UserAgent();
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request);
        userAgent.setOs(userAgentMap.get("os"));
        userAgent.setPersonId(userPrincipal.getPersonId());
        userAgent.setBrowser(userAgentMap.get("browser"));
        userAgent.setLoginTime(new Date());
        userAgent.setNickname(userPrincipal.getNickname());
        userAgent.setUsername(userPrincipal.getUsername());
        userAgent.setLoginTime(new Date());
        String ip = IpAddressUtil.getIp(request);
        userAgent.setIp(ip);
        userAgent.setAddress(IpAddressUtil.getCityInfo(ip));
        return userAgent;
    }

    /**
     * 发起登录成功事件
     * @param request 请求
     * @param userPrincipal 用户实体
     */
    private void loginEvent(HttpServletRequest request, UserPrincipal userPrincipal) {
        log.info("用户【{}】,ip【{}】,登录成功", userPrincipal.getUser().getUsername(), IpAddressUtil.getIp(request));
        EventObject event = new EventObject(userPrincipal, EventObject.Attribute.LOGINLOG);
        event.setAttribute(EventObject.Attribute.REQUEST, request);
        applicationContext.publishEvent(event);
        UserAgent userAgent = getUserAgent(request, userPrincipal);
        userPrincipal.setUserAgent(userAgent);
    }

    public List<Router> routers() {
        return menuService.routers();
    }
}
