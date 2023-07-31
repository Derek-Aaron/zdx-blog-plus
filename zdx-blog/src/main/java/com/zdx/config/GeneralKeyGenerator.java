package com.zdx.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhaodengxuan
 */
@Component("zdx-key-generator")
@Slf4j
public class GeneralKeyGenerator implements KeyGenerator {

	@Override
	public  Object generate( Object target,Method method,Object... params) {
		return "";
	}
}
