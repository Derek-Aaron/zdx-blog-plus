package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_auth
 */
@TableName(value ="us_auth")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "登录项实体")
public class Auth extends BaseTimeEntity {

    @Schema(description = "来源")
    private String source;

    @Schema(description = "用户key")
    private String clientId;

    @Schema(description = "密匙")
    private String secret;

    @Schema(description = "类型")
    private String icon;

    @Schema(description = "回调地址")
    private String callback;

    @Schema(description = "是否禁用")
    private Boolean isEnabled;


    @Serial
    private static final long serialVersionUID = 1L;
}