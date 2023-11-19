package com.zdx.event;

/**
 * @author zhaodengxuan
 */
public interface EventHandle {

	/**
	 * 名称
	 * @return 返回
	 */
	String getKey();

	/**
	 * 执行事件
	 * @param event 事件对象
	 */
	void invokeEvent(EventObject event);
}
