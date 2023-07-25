package com.zdx.config.properties;


import com.zdx.enums.OssTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zdx.oss")
@Data
public class OssProperties {
    /**
     * 使用的oss类型
     */
    private OssTypeEnum ossType = OssTypeEnum.MINIO;
    /**
     * 分布式文件地址
     */
    private String url;

    private String accessKey;

    private String secretKey;

    private String username;

    private String password;
}
