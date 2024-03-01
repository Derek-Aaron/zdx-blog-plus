package com.zdx.service.tk.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.entity.tk.Schedule;
import com.zdx.exception.BaseException;
import com.zdx.mapper.tk.ScheduleMapper;
import com.zdx.schedule.handle.CronUtil;
import com.zdx.schedule.handle.ScheduleUtils;
import com.zdx.service.tk.ScheduleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【tk_schedule】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
    implements ScheduleService{


    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            scheduler.clear();
            List<Schedule> jobs = list();
            for (Schedule job : jobs) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            }
        } catch (SchedulerException e) {
            log.error("初始化任务失败：{}", e.getMessage(), e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSchedule(Schedule schedule) {
        String cron = schedule.getCron();
        if (!CronUtil.isValid(cron)) {
            throw new BaseException("zdx.cron.error");
        }
        if (schedule.getId() != null) {
            return updateSchedule(schedule);
        } else {
            return insertSchedule(schedule);
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSchedule(long id) {
        Schedule schedule = getById(id);
        if (removeById(schedule)) {
            try {
                scheduler.deleteJob(ScheduleUtils.getJobKey(schedule.getId(), schedule.getGroup()));
            } catch (SchedulerException e) {
                log.error("删除任务失败{}", e.getMessage());
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    @Override
    public void run(Long id) {
        Schedule schedule = getById(id);
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(Constants.TASK_PROPERTIES, schedule);
        try {
            scheduler.triggerJob(ScheduleUtils.getJobKey(schedule.getId(), schedule.getGroup()), dataMap);
        } catch (SchedulerException e) {
            log.error("运行调度任务失败");
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<String> ids) {
        for (String id : ids) {
            deleteSchedule(Long.parseLong(id));
        }
        return true;
    }

    private boolean insertSchedule(Schedule schedule) {
        if (save(schedule)) {
            try {
                ScheduleUtils.createScheduleJob(scheduler, schedule);
                return true;
            } catch (SchedulerException e) {
                log.error("创建任务失败：{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private boolean updateSchedule(Schedule schedule) {
        if (saveOrUpdate(schedule)) {
            try {
                Long jobId = schedule.getId();
                JobKey jobKey = ScheduleUtils.getJobKey(jobId, schedule.getGroup());
                if (scheduler.checkExists(jobKey)) {
                    scheduler.deleteJob(jobKey);
                }
                ScheduleUtils.createScheduleJob(scheduler, schedule);
            } catch (SchedulerException e) {
                log.error("创还能任务失败：{}", e.getMessage());
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }
}




