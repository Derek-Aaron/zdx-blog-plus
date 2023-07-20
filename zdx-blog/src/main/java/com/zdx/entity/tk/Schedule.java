package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("定时任务实体")
public class Schedule extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("分组")
    @TableField("group_")
    private String group;

    @ApiModelProperty("执行类")
    private String invoke;

    @ApiModelProperty("执行表达式")
    private String cron;

    @ApiModelProperty("执行策略")
    private String misfire;

    @ApiModelProperty("状态")
    private Boolean status;

    @ApiModelProperty("是否并发")
    private Boolean concurrent;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("描述")
    private String description;

}