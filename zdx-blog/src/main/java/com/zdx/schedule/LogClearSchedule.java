package com.zdx.schedule;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.us.Log;
import com.zdx.schedule.handle.ScheduleHandle;
import com.zdx.service.us.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;


@Component
@Slf4j
@RequiredArgsConstructor
public class LogClearSchedule implements ScheduleHandle {

    private final LogService logService;

    @Override
    public void handle() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, -1);
        Date startTime = DateUtil.beginOfMonth(c.getTime()).toJdkDate();
        Date endTime = DateUtil.endOfMonth(c.getTime()).toJdkDate();
        LambdaQueryWrapper<Log> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Log::getCreateTime, startTime);
        queryWrapper.le(Log::getCreateTime, endTime);
        logService.remove(queryWrapper);
    }
}
