package com.zdx.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhaodengxuan
 */
@Component
@Slf4j
public class GeneralKeyGenerator implements KeyGenerator {

	@Override
	public Object generate(Object target, Method method,Object... params) {
		return generateKey(params);
	}
	public static Object generateKey(Object... params) {
		if (params.length == 0) {
			return "";
		}
		if (params.length == 1) {
			Object param = params[0];
			if (param != null && !param.getClass().isArray()) {
				return param;
			}
		}
		return new SimpleKey(params);
	}
}
