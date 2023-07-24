package com.zdx.config.properties;


import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zdx.es")
@Data
@ConditionalOnExpression("'${zdx.es.open}' == true")
public class EsProperties {

    private String url;

    private String username;

    private String password;

    private Boolean open;

    private String index;
}
