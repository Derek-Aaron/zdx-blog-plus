package com.zdx.schedule.handle;

import cn.hutool.core.util.ObjectUtil;
import com.zdx.Constants;
import com.zdx.entity.tk.Schedule;
import com.zdx.entity.tk.ScheduleLog;
import com.zdx.service.tk.ScheduleLogService;
import com.zdx.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.SchedulingTaskExecutor;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhaodengxuan
 */
@Slf4j
public abstract class AbstractQuartzJob implements Job {
	/**
	 * 线程本地变量
	 */
	private static final Map<Long, Date> REMOVE = new ConcurrentHashMap<>();


	private static  final SchedulingTaskExecutor executor = SpringUtil.getBean(SchedulingTaskExecutor.class);

	@Override
	public void execute(JobExecutionContext context)  {
		Object o = context.getMergedJobDataMap().get(Constants.TASK_PROPERTIES);
		if (o instanceof Schedule schedule) {
			try {
				before(schedule);
				if (ObjectUtil.isNotNull(schedule)) {
					doExecute(context, schedule);
				}
				executor.execute(() -> after(schedule, null));
			} catch (Exception e) {
				log.error("任务执行异常  - ：", e);
				executor.execute(() -> after(schedule, e));
			}
		}
	}
	protected void after(Schedule schedule, Exception e) {
		Date startTime = REMOVE.remove(schedule.getId());
		ScheduleLog jobLog = new ScheduleLog();
		jobLog.setJobId(schedule.getId());
		jobLog.setName(schedule.getName());
		jobLog.setStartTime(startTime);
		jobLog.setOldTime(new Date());
		jobLog.setStatus(Boolean.TRUE);
		if (ObjectUtil.isNotNull(e)) {
			jobLog.setStatus(Boolean.FALSE);
			jobLog.setExceptionInfo(e.getLocalizedMessage());
		}
		SpringUtil.getBean(ScheduleLogService.class).save(jobLog);
	}

	/**
	 * 执行任务
	 * @param context 上下文
	 * @param schedule 实体类
	 * @throws Exception 异常
	 */
	protected abstract void doExecute(JobExecutionContext context, Schedule schedule) throws Exception;

	protected void before(Schedule schedule) {
		REMOVE.put(schedule.getId(), new Date());
	}
}
