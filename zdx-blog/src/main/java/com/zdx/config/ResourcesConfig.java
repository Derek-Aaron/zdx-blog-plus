package com.zdx.config;


import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.math.BigInteger;
import java.util.TimeZone;

@Configuration
@EnableCaching
@EnableAsync
public class ResourcesConfig implements WebMvcConfigurer {

    @Autowired
    private Environment environment;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    @Bean(AbstractApplicationContext.APPLICATION_EVENT_MULTICASTER_BEAN_NAME)
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor, BeanFactory beanFactory) {
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster(beanFactory);
        multicaster.setTaskExecutor(executor);
        return multicaster;
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

}
