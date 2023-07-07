package com.zdx.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaodengxuan
 */
@Component
@ConfigurationProperties(prefix = "zdx.common")
@Data
public class CommonProperties {
	/**
	 * 默认缓存时间
	 */
	private Integer cacheTime = 5;

	/**
	 * 缓存类型
	 */
	private String cache;

	/**
	 * 是否开启xss
	 */
	private Boolean excludes = true;
	/**
	 * xss要过滤的路径
	 */
	private String urlPatterns = "/captcha/**";

	private String[] openPath = {"/login", "/logout","/zdx.auth/all", "/captcha/**", "/desk/**", "/druid/**", "/oauth/**","/ws/**", "/register"};
}
