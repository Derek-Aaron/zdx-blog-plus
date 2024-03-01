package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_comment
 */
@TableName(value ="zdx_comment")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "评论实体")
public class Comment extends BaseTimeEntity {

    @Schema(description = "评论类型")
    private String commentType;

    @Schema(description = "类型id")
    private Long typeId;

    @Schema(description = "父级评论")
    private Long parentId;

    @Schema(description = "回复评论id")
    private Long replyId;

    @Schema(description = "评论点赞数")
    private Long likeCount;

    @Schema(description = "评理内容")
    private String content;

    @Schema(description = "评论人id")
    private Long fromUid;

    @Schema(description = "回复人id")
    private Long toUid;

    @Schema(description = "是否审核通过")
    private Boolean isCheck;


    @Serial
    private static final long serialVersionUID = 1L;
}