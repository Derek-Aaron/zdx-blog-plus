package com.zdx.service.tk;

import com.zdx.entity.tk.ScheduleLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【tk_schedule_log】的数据库操作Service
* @createDate 2023-07-05 17:32:37
*/
public interface ScheduleLogService extends IService<ScheduleLog> {

    /**
     * 清空任务日志
     * @param scheduleId 任务id
     * @return 成功
     */
    boolean clear(String scheduleId);
}
