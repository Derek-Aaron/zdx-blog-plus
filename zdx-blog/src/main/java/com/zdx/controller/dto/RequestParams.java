package com.zdx.controller.dto;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;


@Data
public class RequestParams {


    @ApiModelProperty("加密值")
    private String encrypt;


    @ApiModelProperty("请求参数")
    private Map<String, Object> params = Maps.newHashMap();

    @ApiModelProperty("页数")
    private Integer page;

    @ApiModelProperty("页数大小")
//    @Size(max = 50, message = "{zdx.params.size}")
    private Integer limit;

    public Object getParam(String key) {
        return params.get(key);
    }
    public <T> T getParam(String key, Class<T> clazz, T defaultValue) {
        T param = getParam(key, clazz);
        return ObjUtil.isNull(param) ? defaultValue : param;
    }

    public boolean hasParam(String key) {
        return params.containsKey(key) && ObjUtil.isNotNull(params.get(key)) && StrUtil.isNotBlank(params.get(key).toString());
    }


    @SuppressWarnings("all")
    public <T> T getParam(String key, Class<T> clazz) {
        Object value = params.get(key);
        if (ObjUtil.isNull(value)) {
            return null;
        }
        if (clazz == Integer.class) {
            return clazz.cast(Integer.parseInt(value.toString()));
        }
        if (clazz == Long.class) {
            return clazz.cast(Long.parseLong(value.toString()));
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
}
