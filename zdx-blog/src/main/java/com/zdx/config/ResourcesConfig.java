package com.zdx.config;


import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.zdx.cache.CacheTemplate;
import com.zdx.cache.LocalCacheManager;
import com.zdx.config.properties.CommonProperties;
import com.zdx.filter.xss.XssFilter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
@EnableConfigurationProperties(CommonProperties.class)
@EnableCaching
@EnableAsync
public class ResourcesConfig implements WebMvcConfigurer {

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private Environment environment;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    /**
     * 跨域配置
     */
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

    @Bean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor, BeanFactory beanFactory) {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster(beanFactory);
        multicaster.setTaskExecutor(executor);
        return multicaster;
    }

    @Bean
    @SuppressWarnings("all")
    public FilterRegistrationBean xssFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        Map<String, String> initParameters = new HashMap<>(1);
        initParameters.put("excludes", commonProperties.getExcludes().toString());
        initParameters.put("urlPatterns", commonProperties.getUrlPatterns());
        registration.setInitParameters(initParameters);
        return registration;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            // Long转换成String避免JS超长问题
            builder.serializerByType(Long.class, ToStringSerializer.instance);
            builder.serializerByType(Long.TYPE, ToStringSerializer.instance);
            builder.serializerByType(BigInteger.class, ToStringSerializer.instance);
        };
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/zdx/file/**")
                .addResourceLocations("file:" + environment.getProperty("HOME") + "/zdx/file/");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConditionalOnExpression("'${zdx.common.cache}' == 'local'")
    public CacheTemplate cacheTemplate() {
        return new LocalCacheManager<>();
    }

    @Bean
    @ConditionalOnExpression("'${zdx.common.cache}' == 'local'")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
}
