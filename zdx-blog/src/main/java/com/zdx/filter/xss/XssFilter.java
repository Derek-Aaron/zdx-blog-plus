package com.zdx.filter.xss;


import cn.hutool.core.util.StrUtil;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhaodengxuan
 */
public class XssFilter implements Filter {


	/**
	 * 排除链接
	 */
	public List<String> excludes = new ArrayList<>();

	private final static AntPathMatcher  antPathMatcher = new AntPathMatcher();

	private Boolean open;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		open = Boolean.parseBoolean(filterConfig.getInitParameter("excludes"));
		String tempExcludes = filterConfig.getInitParameter("urlPatterns");
		if (StrUtil.isNotEmpty(tempExcludes)) {
			String[] url = tempExcludes.split(",");
			excludes.addAll(Arrays.asList(url));
		}
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (!open) {
			chain.doFilter(request, response);
			return;
		}
		HttpServletRequest req = (HttpServletRequest) request;
		if (handleExcludeURL(req)) {
			chain.doFilter(request, response);
			return;
		}
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}


	private boolean handleExcludeURL(HttpServletRequest request) {
		String url = request.getRequestURI();
		for (String exclude : excludes) {
			if (null != url && null != exclude && antPathMatcher.match(exclude, url)) {
				return true;
			}
		}
		return false;
	}
}
