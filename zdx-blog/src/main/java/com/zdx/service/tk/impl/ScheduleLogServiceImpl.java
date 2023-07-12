package com.zdx.service.tk.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.tk.ScheduleLog;
import com.zdx.service.tk.ScheduleLogService;
import com.zdx.mapper.tk.ScheduleLogMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【tk_schedule_log】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
public class ScheduleLogServiceImpl extends ServiceImpl<ScheduleLogMapper, ScheduleLog>
    implements ScheduleLogService{

    @Override
    public boolean clear(String scheduleId) {
        LambdaQueryWrapper<ScheduleLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduleLog::getJobId, scheduleId);
        return remove(queryWrapper);
    }
}




