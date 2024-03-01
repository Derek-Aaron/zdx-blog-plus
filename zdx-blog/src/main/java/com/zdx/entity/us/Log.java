package com.zdx.entity.us;

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
 * @TableName us_log
 */
@TableName(value ="us_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "日志实体")
public class Log extends BaseEntity {

    @Schema(description = "日志事件")
    private String event;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "访问链接")
    private String url;

    @Schema(description = "日志内容")
    private String content;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "系统")
    private String os;

    @Schema(description = "浏览器")
    private String browser;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "状态")
    private Boolean status;

    @Serial
    private static final long serialVersionUID = 1L;
}