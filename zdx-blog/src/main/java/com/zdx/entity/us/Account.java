package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_account
 */
@TableName(value ="us_account")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户账号实体")
public class Account extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    @JsonIgnore
    private String password;

    @ApiModelProperty("是否禁用")
    private Boolean isDisabled;

    @ApiModelProperty("是否锁定")
    private Boolean isLocked;

    @ApiModelProperty("用户id")
    private Long userId;

}