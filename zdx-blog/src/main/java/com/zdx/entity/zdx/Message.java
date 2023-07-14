package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_message
 */
@TableName(value ="zdx_message")
@Data
public class Message implements Serializable {
    private Long id;

    private String nickname;

    private String avatar;

    private String content;

    private String ip;

    private String source;

    private Boolean isCheck;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}