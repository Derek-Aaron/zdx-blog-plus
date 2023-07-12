package com.zdx.filter.xss;

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
		body = request.getInputStream().readAllBytes();
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			int length = values.length;
			String[] escapseValues = new String[length];
			for (int i = 0; i < length; i++) {
				String val = values[i];
				if (StrUtil.isNotBlank(val)) {
					// 防xss攻击和过滤前后空格
					escapseValues[i] = new HTMLFilter().filter(val).trim();
//					escapseValues[i] = Objects.requireNonNull(SensitiveUtil.filter(val)).trim();
				} else {
					escapseValues[i] = val;
				}
			}
			return escapseValues;
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

		// xss过滤
		json = new HTMLFilter().filter(json).trim();
		//敏感信息过滤
//		json = SensitiveUtil.filter(json);
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
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
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
