package com.zdx.schedule.handle;

import com.zdx.entity.tk.Schedule;
import org.quartz.JobExecutionContext;

/**
 * @author zhaodengxuan
 */
public class QuartzJobExecution extends AbstractQuartzJob{
	@Override
	protected void doExecute(JobExecutionContext context, Schedule schedule) throws Exception {
		JobInvokeUtil.invokeMethod(schedule);
	}
}
