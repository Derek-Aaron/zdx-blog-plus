package com.zdx.service.tk;

import com.zdx.entity.tk.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【tk_schedule】的数据库操作Service
* @createDate 2023-07-05 17:32:37
*/
public interface ScheduleService extends IService<Schedule> {
    /**
     * 保存定时任务
     * @param schedule 任务
     * @return 成功
     */
    boolean saveSchedule(Schedule schedule);

    /**
     * 删除任务
     * @param id 任务id
     * @return 成功
     */
    boolean deleteSchedule(long id);

    /**
     * 执行一次任务
     * @param id 任务id
     */
    void run(Long id);

    /**
     * 批量删除任务
     * @param ids 任务id
     * @return 成功
     */
    boolean batchDelete(List<String> ids);
}
