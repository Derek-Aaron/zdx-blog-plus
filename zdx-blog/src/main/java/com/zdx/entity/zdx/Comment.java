package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
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
public class Comment extends BaseTimeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String commentType;

    private Long typeId;

    private Long parentId;

    private Long replyId;

    private String content;

    private Long fromUid;

    private Long toUid;

    private Boolean isCheck;

    @ApiModelProperty("评论人名称")
    @TableField(exist = false)
    private String fromName;

    @ApiModelProperty("回复人名称")
    @TableField(exist = false)
    private String toName;



}