package com.zdx.service.us.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Log;
import com.zdx.service.us.LogService;
import com.zdx.mapper.us.LogMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【us_log】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log>
    implements LogService{

    @Override
    public boolean clear(String event) {
        LambdaQueryWrapper<Log> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(event)) {
            queryWrapper.eq(Log::getEvent, event);
        }
        return remove(queryWrapper);
    }
}




