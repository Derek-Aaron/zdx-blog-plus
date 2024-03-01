package com.zdx.filter.read;


import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

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
        return getServletInputStream(body);
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
            public int available() {
                return body.length;
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
