package com.zdx.utils;

import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

/**
 * @author zhaodengxuan
 */
@Component
public class SpringUtil  implements BeanFactoryPostProcessor, ApplicationContextAware {
	private static ApplicationContext applicationContext;

	private static ConfigurableListableBeanFactory beanFactory;

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		SpringUtil.beanFactory = configurableListableBeanFactory;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static ListableBeanFactory getBeanFactory() {
		return null == beanFactory ? applicationContext : beanFactory;
	}

	public static ConfigurableListableBeanFactory getConfigurableBeanFactory() throws UtilException {
		ConfigurableListableBeanFactory factory;
		if (null != beanFactory) {
			factory = beanFactory;
		} else {
			if (!(applicationContext instanceof ConfigurableApplicationContext)) {
				throw new RuntimeException("No ConfigurableListableBeanFactory from context!");
			}

			factory = ((ConfigurableApplicationContext)applicationContext).getBeanFactory();
		}

		return factory;
	}

	@SuppressWarnings("all")
	public static <T> T getBean(String name) {
		return (T) getBeanFactory().getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return getBeanFactory().getBean(clazz);
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return getBeanFactory().getBean(name, clazz);
	}

	@SuppressWarnings("all")
	public static <T> T getBean(TypeReference<T> reference) {
		ParameterizedType parameterizedType = (ParameterizedType)reference.getType();
		Class<T> rawType = (Class)parameterizedType.getRawType();
		Class<?>[] genericTypes = (Class[]) Arrays.stream(parameterizedType.getActualTypeArguments()).map((type) -> {
			return (Class)type;
		}).toArray((x$0) -> {
			return new Class[x$0];
		});
		String[] beanNames = getBeanFactory().getBeanNamesForType(ResolvableType.forClassWithGenerics(rawType, genericTypes));
		return getBean(beanNames[0], rawType);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return getBeanFactory().getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return getBeanFactory().getBeanNamesForType(type);
	}

	public static String getProperty(String key) {
		return null == applicationContext ? null : applicationContext.getEnvironment().getProperty(key);
	}

	public static String getApplicationName() {
		return getProperty("spring.application.name");
	}

	public static String[] getActiveProfiles() {
		return null == applicationContext ? null : applicationContext.getEnvironment().getActiveProfiles();
	}

	public static String getActiveProfile() {
		String[] activeProfiles = getActiveProfiles();
		return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	public static <T> void registerBean(String beanName, T bean) {
		ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		factory.autowireBean(bean);
		factory.registerSingleton(beanName, bean);
	}

	public static void unregisterBean(String beanName) {
		ConfigurableListableBeanFactory factory = getConfigurableBeanFactory();
		if (factory instanceof DefaultSingletonBeanRegistry) {
			DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry)factory;
			registry.destroySingleton(beanName);
		} else {
			throw new UtilException("Can not unregister bean, the factory is not a DefaultSingletonBeanRegistry!");
		}
	}

	public static void publishEvent(ApplicationEvent event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}

	}

	public static void publishEvent(Object event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}

	}

}
