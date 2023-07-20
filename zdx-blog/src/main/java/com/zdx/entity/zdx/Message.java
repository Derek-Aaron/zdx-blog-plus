package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_message
 */
@TableName(value ="zdx_message")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("留言消息实体")
public class Message extends BaseTimeEntity {

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

    @ApiModelProperty("是否审核通过")
    private Boolean isCheck;

    @Serial
    private static final long serialVersionUID = 1L;
}