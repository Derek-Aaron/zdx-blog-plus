package com.zdx.service.tk;

import cn.hutool.core.util.ObjUtil;
import com.zdx.entity.tk.Config;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【tk_config】的数据库操作Service
* @createDate 2023-07-05 17:32:37
*/
public interface ConfigService extends IService<Config> {


    <T> T getConfig(String key, Class<T> clazz);


    Object getConfig(String key);

    Boolean setConfig(String key, Object val);


    @SuppressWarnings("all")
    default <T> T getConfig(String key, Class<T> clazz, Object defaultValue) {
        T val = getConfig(key, clazz);
        return ObjUtil.isNotNull(val) ? val : (T) defaultValue;
    }

}
