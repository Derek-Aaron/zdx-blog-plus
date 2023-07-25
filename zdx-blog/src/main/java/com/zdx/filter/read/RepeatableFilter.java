package com.zdx.filter.read;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RepeatableFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RepeatableHttpServletRequestWrapper repeatableRequest = new RepeatableHttpServletRequestWrapper((HttpServletRequest) servletRequest);
        filterChain.doFilter(repeatableRequest, servletResponse);
    }
}
