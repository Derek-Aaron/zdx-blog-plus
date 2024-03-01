package com.zdx.security;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSON;
import com.zdx.handle.Result;
import com.zdx.security.service.PermissionService;
import com.zdx.security.vo.UserSession;
import com.zdx.utils.MessageUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zhaodengxuan
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {


	@Autowired
	private PermissionService permissionService;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type","text/html;charset=UTF-8");
		UserSession userInfo = permissionService.getLoginUserInfo(request);
		if (ObjUtil.isNotNull(userInfo)) {
			permissionService.logout(userInfo.getPersonId());
		}
		PrintWriter writer = response.getWriter();
		writer.println(JSON.toJSONString(Result.success(MessageUtil.message("zdx.logout.success"))));
	}
}
