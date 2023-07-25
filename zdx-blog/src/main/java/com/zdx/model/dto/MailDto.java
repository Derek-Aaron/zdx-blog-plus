package com.zdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@ApiModel(description = "邮箱DTO")
@Builder
public class MailDto {

    /**
     * 接收者邮箱号
     */
    @ApiModelProperty(value = "接收者邮箱号")
    private String toEmail;

    /**
     * 主题
     */
    @ApiModelProperty(value = "主题")
    private String subject;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
     * 内容信息
     */
    @ApiModelProperty(value = "内容信息")
    private Map<String, Object> contentMap;

    /**
     * 邮件模板
     */
    @ApiModelProperty(value = "邮件模板")
    private String template;
}
