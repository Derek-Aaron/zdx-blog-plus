package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_chat_record
 */
@TableName(value ="zdx_chat_record")
@Data
public class ChatRecord implements Serializable {
    private Long id;

    private Long userId;

    private String nickname;

    private String avatar;

    private String content;

    private String ip;

    private String source;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}