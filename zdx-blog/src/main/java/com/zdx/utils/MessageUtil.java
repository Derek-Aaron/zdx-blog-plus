package com.zdx.utils;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author zhaodengxuan
 */
@Component
public class MessageUtil implements MessageSourceAware {
	private static MessageSource defaultMessageSource;

	private static MessageSource customMessageSource;

	public static  String message(String code, Object... args) {
		return getLocaleMessage(code);
	}



	private static String[] resources;

	public static String[] getResources() {
		if (resources == null) {
			return new String[0];
		}
		System.arraycopy(resources, 0, new String[resources.length], 0, resources.length);
		return ArrayUtil.clone(resources);
	}

	public static MessageSource getDefaultMessageSource() {
		return defaultMessageSource;
	}

	public static void initDefaultMessageResources(String[] resources) {
		MessageUtil.resources = (resources == null ? null : ArrayUtil.clone(resources));
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		if (resources != null) {
			ms.setBasenames(resources);
		}
		ms.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
		ms.setDefaultEncoding("utf-8");
		setDefaultMessageSource(ms);
	}

	public static void setDefaultMessageSource(MessageSource defaultMessageSource) {
		MessageUtil.defaultMessageSource = defaultMessageSource;
	}

	public static MessageSource getCustomMessageSource() {
		return customMessageSource;
	}

	public static void setCustomMessageSource(MessageSource customMessageSource) {
		MessageUtil.customMessageSource = customMessageSource;
	}

	public static String getLocaleMessage(String name) {
		return getLocaleMessage(name, null);
	}

	public static String getLocaleMessage(String name, String defaultMsg) {
		String message = null;
		if (customMessageSource != null) {
			message = customMessageSource.getMessage(name, null, null, Locale.getDefault());
		}
		if (message == null && defaultMessageSource != null) {
			message = defaultMessageSource.getMessage(name, null, defaultMsg, Locale.getDefault());
		}
		return message;
	}

	public static String getLocaleMessage(Enum<?> obj, String defaultMsg) {
		return getLocaleMessage(obj.getClass().getName() + "." + obj.name(), defaultMsg);
	}

	public static String getLocaleMessage(Enum<?> obj) {
		return getLocaleMessage(obj, obj.name());
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		defaultMessageSource = messageSource;
	}
}
