package com.zdx.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseTimeEntity extends BaseEntity{


    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "更新时间")
    private Date updateTime;
}
