package com.zdx.config;


import com.zdx.config.properties.CommonProperties;
import com.zdx.filter.read.RepeatableFilter;
import com.zdx.filter.sensitive.SensitiveFilter;
import com.zdx.filter.xss.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(CommonProperties.class)
public class FilterRegistrationConfig {

    @Autowired
    private CommonProperties commonProperties;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOriginPattern("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 有效期 1800秒
        config.setMaxAge(1800L);
        // 添加映射路径，拦截一切请求
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // 返回新的CorsFilter
        return new CorsFilter(source);
    }

    /**
     * 注册过滤器可以使request中的流实现重复读取
     * @return 返回
     */
    @Bean
    public FilterRegistrationBean<Filter> repeatableFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new RepeatableFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> xssFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 1);
        Map<String, String> initParameters = new HashMap<>(1);
        initParameters.put("excludes", commonProperties.getXssExcludes().toString());
        initParameters.put("urlPatterns", commonProperties.getXssUrlPatterns());
        registration.setInitParameters(initParameters);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Filter> sensitiveFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new SensitiveFilter());
        registration.addUrlPatterns("/*");
        registration.setName("sensitiveFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE + 2);
        Map<String, String> initParameters = new HashMap<>(1);
        initParameters.put("excludes", commonProperties.getSensitiveExcludes().toString());
        initParameters.put("urlPatterns", commonProperties.getSensitiveUrlPatterns());
        registration.setInitParameters(initParameters);
        return registration;
    }
}
