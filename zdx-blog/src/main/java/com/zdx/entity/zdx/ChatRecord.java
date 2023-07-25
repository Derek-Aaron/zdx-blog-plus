package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @TableName zdx_chat_record
 */
@TableName(value ="zdx_chat_record")
@Data
@EqualsAndHashCode(callSuper = true)
public class ChatRecord extends BaseEntity {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("来源")
    private String source;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}