package com.zdx.schedule.handle;

import com.zdx.entity.tk.Schedule;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * @author zhaodengxuan
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob{
	@Override
	protected void doExecute(JobExecutionContext context, Schedule schedule) throws Exception {
		JobInvokeUtil.invokeMethod(context,schedule);
	}
}
