package com.zdx.service.us;

import com.zdx.entity.us.Log;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【us_log】的数据库操作Service
* @createDate 2023-07-05 17:30:41
*/
public interface LogService extends IService<Log> {

    boolean clear(String event);
}
