package com.zdx.annotation;

import com.zdx.enums.LogEventEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    LogEventEnum type() default LogEventEnum.OTHER;

    String desc() default "";  // 操作说明

    boolean save() default true; //是否将当前日志记录到数据库中
}
