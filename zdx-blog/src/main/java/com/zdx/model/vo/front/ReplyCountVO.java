package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "回复数VO")
public class ReplyCountVO {

    /**
     * 评论id
     */
    @Schema(description = "评论id")
    private Long commentId;

    /**
     * 回复数
     */
    @Schema(description = "回复数")
    private Long replyCount;
}
