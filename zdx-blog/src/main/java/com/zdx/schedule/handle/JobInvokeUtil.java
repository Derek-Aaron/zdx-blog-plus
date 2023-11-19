package com.zdx.schedule.handle;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zdx.entity.tk.Schedule;
import com.zdx.utils.SpringUtil;

import java.util.Map;

/**
 * @author zhaodengxuan
 */
public class JobInvokeUtil {

	public static void invokeMethod(Schedule schedule) throws Exception {
		String invokeTarget = schedule.getInvoke();
		String backName = StrUtil.subSuf(invokeTarget, invokeTarget.lastIndexOf(":") + 1);
		Class<?> clz = Class.forName(backName);
		Map<String, ?> beanMap = SpringUtil.getApplicationContext().getBeansOfType(clz);
		if (!beanMap.isEmpty()) {
			for (Map.Entry<?, ?> entry : beanMap.entrySet()) {
				if (entry.getValue().getClass().getName().equals(backName)) {
					ScheduleHandle taskHandle = (ScheduleHandle) entry.getValue();
					if (ObjectUtil.isNotNull(taskHandle)) {
						taskHandle.handle();
					}
				}
			}
		}
	}
}
