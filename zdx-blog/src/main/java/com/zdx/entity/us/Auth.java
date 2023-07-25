package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_auth
 */
@TableName(value ="us_auth")
@Data
@EqualsAndHashCode(callSuper = true)
public class Auth extends BaseTimeEntity {

    private String username;

    private String source;

    private Long userId;

    private String clientId;

    private String secret;

    private String callback;

    private String type;

    private String icon;

    private Boolean isEnabled;


    @Serial
    private static final long serialVersionUID = 1L;
}