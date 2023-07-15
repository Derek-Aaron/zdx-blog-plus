package com.zdx.filter.sensitive;

import cn.hutool.core.util.StrUtil;
import com.zdx.utils.SensitiveUtil;
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

public class SensitiveHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public SensitiveHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }


    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        String json = IOUtils.toString(super.getInputStream(), StandardCharsets.UTF_8);
        json = SensitiveUtil.filter(json);
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
            public int read() throws IOException {
                return bis.read();
            }
        };
    }
}