package com.zdx.filter;


import cn.hutool.core.util.StrUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author zhaodengxuan
 */
public class XssFilter implements Filter {


	/**
	 * 排除链接
	 */
	public List<String> excludes = new ArrayList<>();

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String tempExcludes = filterConfig.getInitParameter("urlPatterns");
		if (StrUtil.isNotEmpty(tempExcludes)) {
			String[] url = tempExcludes.split(",");
			excludes.addAll(Arrays.asList(url));
		}
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		if (!handleExcludeURL(req, resp)) {
			chain.doFilter(request, response);
			return;
		}
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}


	private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
		String url = request.getRequestURI();
		for (String exclude : excludes) {
			return null != url && null != exclude && Pattern.matches(url, exclude);
		}
		return false;
	}
}
