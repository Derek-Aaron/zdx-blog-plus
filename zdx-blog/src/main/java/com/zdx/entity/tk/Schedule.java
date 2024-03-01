package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "定时任务实体")
public class Schedule extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "分组")
    @TableField("group_")
    private String group;

    @Schema(description = "执行类")
    private String invoke;

    @Schema(description = "执行表达式")
    private String cron;

    @Schema(description = "执行策略")
    private String misfire;

    @Schema(name = "状态")
    private Boolean status;

    @Schema(description = "是否并发")
    private Boolean concurrent;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "描述")
    private String description;

}