package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * @TableName zdx_chat_record
 */
@TableName(value ="zdx_chat_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecord extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private String nickname;

    private String avatar;

    private String content;

    private String ip;

    private String source;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}