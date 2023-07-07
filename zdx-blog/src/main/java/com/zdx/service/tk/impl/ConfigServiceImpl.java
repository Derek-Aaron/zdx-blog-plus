package com.zdx.service.tk.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.tk.Config;
import com.zdx.service.tk.ConfigService;
import com.zdx.mapper.tk.ConfigMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【tk_config】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config>
    implements ConfigService{

}




