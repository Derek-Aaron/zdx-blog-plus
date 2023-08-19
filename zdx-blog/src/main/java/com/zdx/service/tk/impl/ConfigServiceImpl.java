package com.zdx.service.tk.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.tk.Config;
import com.zdx.enums.ConfigTypeEnum;
import com.zdx.mapper.tk.ConfigMapper;
import com.zdx.service.tk.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【tk_config】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config>
    implements ConfigService{

    @Override
    @SuppressWarnings("all")
    public <T> T getConfig(String key, Class<T> clazz) {
        Object value = getConfig(key);
        if (ObjUtil.isNull(value)) {
            return null;
        }
        if (clazz == Integer.class) {
            return clazz.cast(Integer.parseInt(value.toString()));
        }
        if (clazz == Long.class) {
            return clazz.cast(Long.parseLong(value.toString()));
        }
        if (clazz == Boolean.class) {
            return clazz.cast(Boolean.parseBoolean(value.toString()));
        }
        if (clazz == Double.class) {
            return clazz.cast(Double.parseDouble(value.toString()));
        }
        if (clazz == Short.class) {
            return clazz.cast(Short.parseShort(value.toString()));
        }
        if (clazz == Byte.class) {
            return clazz.cast(Byte.parseByte(value.toString()));
        }
        if (clazz == Float.class) {
            return clazz.cast(Float.parseFloat(value.toString()));
        }
        if (clazz == String.class) {
            return clazz.cast(value.toString());
        }
        if (clazz == Date.class) {
            return clazz.cast(DateUtil.parse(value.toString()).toJdkDate());
        }
        if (clazz == Enum.class) {
            return clazz.cast(Enum.valueOf((Class<? extends Enum>)clazz, value.toString()));
        }
        if (clazz == List.class) {
            if (value.toString().contains(",")) {
                return clazz.cast( Arrays.asList(value.toString().split(",")));
            } else {
                return clazz.cast(Collections.singletonList(value.toString()));
            }
        }
        return (T) value;
    }

    @Override
    public Object getConfig(String key) {
        LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Config::getName, key);
        Config config = getOne(queryWrapper);
        Object val = null;
        if (ObjUtil.isNotNull(config)) {
           val = config.getValue();
        }
        return val;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setConfig(String key, Object val) {
        if (ObjUtil.isNull(val)) {
            return false;
        }
        LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Config::getName, key);
        Config config = getOne(queryWrapper);
        if (ObjUtil.isNull(config)) {
            config = new Config();
            config.setName(key);
        }
        config.setType(ConfigTypeEnum.STRING.name());
        config.setValue(val.toString());
        return saveOrUpdate(config);
    }
}




