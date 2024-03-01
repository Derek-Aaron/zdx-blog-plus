package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_message
 */
@TableName(value ="zdx_message")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "留言消息实体")
public class Message extends BaseTimeEntity {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "来源")
    private String source;

    @Schema(description = "是否审核通过")
    private Boolean isCheck;

    @Serial
    private static final long serialVersionUID = 1L;
}