package com.zdx.schedule;

import com.zdx.schedule.handle.ScheduleHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ScheduleTest implements ScheduleHandle {
    @Override
    public void handle() {
        log.info("执行啦");
    }
}
