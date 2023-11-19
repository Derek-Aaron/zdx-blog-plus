package com.zdx.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author zhaodengxuan
 */
@Component("zdx-key-generator")
@Slf4j
public class GeneralKeyGenerator implements KeyGenerator {

	@NotNull
	@Override
	public  Object generate(@NotNull Object target, @NotNull Method method, @NotNull Object... params) {
		return "";
	}
}
