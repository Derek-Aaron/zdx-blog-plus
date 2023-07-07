package com.zdx.schedule.handle;

import com.zdx.Constants;
import com.zdx.entity.tk.Schedule;
import com.zdx.enums.MisfireTypeEnum;
import org.quartz.*;

/**
 * @author zhaodengxuan
 */
public class ScheduleUtils {
	/**
	 * 得到quartz任务类
	 *
	 * @param schedule 执行计划
	 * @return 具体执行任务类
	 */
	private static Class<? extends Job> getQuartzJobClass(Schedule schedule) {
		return schedule.getConcurrent() ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
	}

	/**
	 * 构建任务触发对象
	 */
	public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
		return TriggerKey.triggerKey(Constants.TASK_CLASS_NAME + jobId, jobGroup);
	}

	/**
	 * 构建任务键对象
	 */
	public static JobKey getJobKey(Long jobId, String jobGroup) {
		return JobKey.jobKey(Constants.TASK_CLASS_NAME + jobId, jobGroup);
	}

	public static void createScheduleJob(Scheduler scheduler, Schedule schedule) throws SchedulerException {
		Class<? extends Job> jobClass = getQuartzJobClass(schedule);
		// 构建job信息
		Long jobId = schedule.getId();
		String jobGroup = schedule.getGroup();
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();
		// 表达式调度构建器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCron());
		cronScheduleBuilder = handleCronScheduleMisfirePolicy(schedule, cronScheduleBuilder);
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup)).withSchedule(cronScheduleBuilder).build();
		// 放入参数，运行时的方法可以获取
		jobDetail.getJobDataMap().put(Constants.TASK_PROPERTIES, schedule);
		// 判断是否存在
		if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
			// 防止创建时存在数据问题 先移除，然后在执行创建操作
			scheduler.deleteJob(getJobKey(jobId, jobGroup));
		}
		scheduler.scheduleJob(jobDetail, trigger);
		// 暂停任务
		if (!schedule.getStatus()) {
			scheduler.pauseJob(getJobKey(jobId, jobGroup));
		}
	}

	/**
	 * 设置定时任务策略
	 */
	public static CronScheduleBuilder handleCronScheduleMisfirePolicy(Schedule schedule, CronScheduleBuilder cb) {
		if (schedule.getMisfire().equals(MisfireTypeEnum.DEFAULT.name())) {
			return cb;
		} else if (schedule.getMisfire().equals(MisfireTypeEnum.IMMEDIATELY.name())) {
			return cb.withMisfireHandlingInstructionIgnoreMisfires();
		} else if (schedule.getMisfire().equals(MisfireTypeEnum.ONCE.name())) {
			return cb.withMisfireHandlingInstructionFireAndProceed();
		} else if (schedule.getMisfire().equals(MisfireTypeEnum.NOTTRIGGER.name())) {
			return cb.withMisfireHandlingInstructionDoNothing();
		} else {
			return cb;
		}
	}
}
