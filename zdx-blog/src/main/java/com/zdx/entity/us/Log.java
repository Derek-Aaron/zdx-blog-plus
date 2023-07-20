package com.zdx.entity.us;

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
 * @TableName us_log
 */
@TableName(value ="us_log")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("日志实体")
public class Log extends BaseEntity {

    @ApiModelProperty("日志事件")
    private String event;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("访问链接")
    private String url;

    @ApiModelProperty("日志内容")
    private String content;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("系统")
    private String os;

    @ApiModelProperty("浏览器")
    private String browser;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("状态")
    private Boolean status;

    @Serial
    private static final long serialVersionUID = 1L;
}