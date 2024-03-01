package com.zdx.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j 配置类
 */

@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("博客API文档")
                .description("基于SpringBoot + Vue开发的博客项目")
                .version("0.1")
                .contact(new Contact()
                        .name("zdx")
                        .email("2488288090@qq.com")
                        .url("https://www.zhaodengxuan.top")
                )
        );
    }
}
