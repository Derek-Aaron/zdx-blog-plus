package com.zdx.filter.xss;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	public XssHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		//防止getParameter失效
		getRequest().getParameterMap();
		body = getRequest().getInputStream().readAllBytes();
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (ObjUtil.isNotNull(values)) {
			String[] str = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				String val = new HTMLFilter().filter(values[i]).trim();
				str[i]  = val;
			}
			return str;
		}
		return super.getParameterValues(name);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// 非json类型，直接返回
		if (!isJsonRequest()) {
			return getServletInputStream(body);
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(body);
		// 为空，直接返回
		String json = IOUtils.toString(bis, StandardCharsets.UTF_8);
		if (StrUtil.isEmpty(json)) {
			return getServletInputStream(body);
		}
		//数据xss过滤
		json = new HTMLFilter().filter(json).trim();
		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		return getServletInputStream(bytes);
	}
	/**
	 * 是否是Json请求
	 *
	 */
	public boolean isJsonRequest() {
		String header = super.getHeader(HttpHeaders.CONTENT_TYPE);
		return StringUtils.startsWithIgnoreCase(header, MediaType.APPLICATION_JSON_VALUE);
	}


	public ServletInputStream getServletInputStream(byte[] body) {
		ByteArrayInputStream bis = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public boolean isFinished() {
				return bis.available() == 0;
			}

			@Override
			public boolean isReady() {
				return true;
			}

			@Override
			public int available() throws IOException {
				return body.length;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() throws IOException {
				return bis.read();
			}
		};
	}
}
