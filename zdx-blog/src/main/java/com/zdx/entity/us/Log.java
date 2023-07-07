package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * @TableName us_log
 */
@TableName(value ="us_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class Log extends BaseEntity {

    private String event;

    private String username;

    private String url;

    private String content;

    private String ip;

    private String source;

    private String os;

    private String browser;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long userId;

    private Boolean status;

    @Serial
    private static final long serialVersionUID = 1L;
}