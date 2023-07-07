package com.zdx.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhaodengxuan
 */
@Component
public class ApplicationListener implements org.springframework.context.ApplicationListener<EventObject> {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void onApplicationEvent(EventObject event) {
		Map<String, EventHandle> beans = applicationContext.getBeansOfType(EventHandle.class);
		beans.forEach((key, value) -> {
			if (event.getName().equals(value.getKey())) {
				value.invokeEvent(event);
			}
		});
	}
}
