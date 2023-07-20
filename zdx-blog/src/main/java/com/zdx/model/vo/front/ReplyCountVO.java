package com.zdx.model.vo.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "回复数VO")
public class ReplyCountVO {

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    /**
     * 回复数
     */
    @ApiModelProperty(value = "回复数")
    private Long replyCount;
}
