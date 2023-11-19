package com.zdx.filter.xss;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.io.IOUtils;

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


	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

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
		String json = IOUtils.toString(super.getInputStream(), StandardCharsets.UTF_8);
		json = new HTMLFilter().filter(json);
		if (StrUtil.isBlank(json)) {
			return super.getInputStream();
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
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
			public void setReadListener(ReadListener readListener) {

			}

			@Override
			public int read() {
				return bis.read();
			}
		};
	}

}
