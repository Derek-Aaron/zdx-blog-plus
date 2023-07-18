package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_message
 */
@TableName(value ="zdx_message")
@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends BaseTimeEntity {

    private String nickname;

    private String avatar;

    private String content;

    private String ip;

    private String source;

    private Boolean isCheck;

    private static final long serialVersionUID = 1L;
}