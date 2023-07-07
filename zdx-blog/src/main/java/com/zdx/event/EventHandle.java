package com.zdx.event;

/**
 * @author zhaodengxuan
 */
public interface EventHandle {

	/**
	 * 名称
	 * @return
	 */
	String getKey();

	/**
	 * 执行事件
	 * @param event
	 */
	void invokeEvent(EventObject event);
}
