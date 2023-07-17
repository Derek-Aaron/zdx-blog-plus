package com.zdx.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReplyCountVO {

    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 回复数
     */
    @ApiModelProperty(value = "回复数")
    private Integer replyCount;
}
