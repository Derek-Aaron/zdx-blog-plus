package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_auth
 */
@TableName(value ="us_auth")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("登录项实体")
public class Auth extends BaseTimeEntity {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户key")
    private String clientId;

    @ApiModelProperty("密匙")
    private String secret;

    @ApiModelProperty("回调地址")
    private String callback;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("类型")
    private String icon;

    @ApiModelProperty("是否禁用")
    private Boolean isEnabled;


    @Serial
    private static final long serialVersionUID = 1L;
}