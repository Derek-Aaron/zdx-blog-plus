package com.zdx.security;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSON;
import com.zdx.entity.us.User;
import com.zdx.handle.Result;
import com.zdx.security.service.PermissionService;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private PermissionService permissionService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        UserSession userSession = permissionService.getLoginUserInfo(request);
        if (ObjUtil.isNotNull(userSession)) {
            if (userSession.getUserId().toString().equals(User.PRESENTATION_USER_ID)) {
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writer.println(JSON.toJSONString(Result.error(500, MessageUtil.message("zdx.presentation.error"))));
                return;
            }
            permissionService.refreshToken(userSession);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userSession, null, ((UserPrincipal)userSession).getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // 生成token
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
