package com.zdx.security;


import com.alibaba.fastjson.JSON;
import com.zdx.handle.Result;
import com.zdx.utils.MessageUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * @author zhaodengxuan
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Content-type","text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		writer.println(JSON.toJSONString(Result.error(403, MessageUtil.message("zdx.authentication.error"))));
	}
}
