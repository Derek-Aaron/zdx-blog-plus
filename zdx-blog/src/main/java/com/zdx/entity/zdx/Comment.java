package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_comment
 */
@TableName(value ="zdx_comment")
@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseTimeEntity {

    private String commentType;

    private Long typeId;

    private Long parentId;

    private Long replyId;

    private Long likeCount;

    private String content;

    private Long fromUid;

    private Long toUid;

    private Boolean isCheck;


    private static final long serialVersionUID = 1L;
}