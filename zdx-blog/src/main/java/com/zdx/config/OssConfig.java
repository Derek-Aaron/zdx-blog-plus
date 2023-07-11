package com.zdx.config;


import com.zdx.config.properties.OssProperties;
import com.zdx.enums.OssTypeEnum;
import com.zdx.oss.FileTemplate;
import com.zdx.oss.FileTemplateImpl;
import com.zdx.oss.MinioTemplateImpl;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssConfig {

    @Autowired
    private OssProperties ossProperties;

    @Bean
    @ConditionalOnExpression("'${zdx.oss.oss-type}'=='minio'")
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(ossProperties.getUrl())
                .credentials(ossProperties.getAccessKey(), ossProperties.getSecretKey())
                .build();
    }

    @Bean
    public FileTemplate fileTemplate() {
        if (ossProperties.getOssType().name().equals(OssTypeEnum.MINIO.name())) {
            return new MinioTemplateImpl();
        }
        return new FileTemplateImpl();
    }
}
