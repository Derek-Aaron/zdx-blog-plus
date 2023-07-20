package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_comment
 */
@TableName(value ="zdx_comment")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("评论实体")
public class Comment extends BaseTimeEntity {

    @ApiModelProperty("评论类型")
    private String commentType;

    @ApiModelProperty("类型id")
    private Long typeId;

    @ApiModelProperty("父级评论")
    private Long parentId;

    @ApiModelProperty("回复评论id")
    private Long replyId;

    @ApiModelProperty("评论点赞数")
    private Long likeCount;

    @ApiModelProperty("评理内容")
    private String content;

    @ApiModelProperty("评论人id")
    private Long fromUid;

    @ApiModelProperty("回复人id")
    private Long toUid;

    @ApiModelProperty("是否审核通过")
    private Boolean isCheck;


    @Serial
    private static final long serialVersionUID = 1L;
}