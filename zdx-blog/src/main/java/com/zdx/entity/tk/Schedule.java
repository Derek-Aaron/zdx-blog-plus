package com.zdx.entity.tk;

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
 * @TableName tk_schedule
 */
@TableName(value ="tk_schedule")
@Data
@EqualsAndHashCode(callSuper = true)
public class Schedule extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    @TableField("group_")
    private String group;

    private String invoke;

    private String cron;

    private String misfire;

    private Boolean status;

    private Boolean concurrent;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String description;

}