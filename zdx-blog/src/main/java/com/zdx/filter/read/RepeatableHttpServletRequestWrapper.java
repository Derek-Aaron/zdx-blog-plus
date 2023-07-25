package com.zdx.filter.read;

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

public class RepeatableHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;
    public RepeatableHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        //防止getParameter失效
        getRequest().getParameterMap();
        body = getRequest().getInputStream().readAllBytes();
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!isJsonRequest()) {
            return getServletInputStream(body);
        }
        return getServletInputStream(body);
    }

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
