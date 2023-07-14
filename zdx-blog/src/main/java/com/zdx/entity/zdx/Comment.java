package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_comment
 */
@TableName(value ="zdx_comment")
@Data
public class Comment implements Serializable {
    private Long id;

    private String commentType;

    private Long typeId;

    private Long parentId;

    private Long replyId;

    private String content;

    private Long fromUid;

    private Long toUid;

    private Boolean isCheck;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}