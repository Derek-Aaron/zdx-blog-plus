package com.zdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("更改用户信息")
public class UserStatus {

    @ApiModelProperty("用户id")
    @NotNull
    private Long userId;

    @ApiModelProperty("类型(disabled)禁用(locked)锁定")
    @NotBlank
    private String type;

    @ApiModelProperty("禁用")
    private Boolean isDisabled;

    @ApiModelProperty("锁定")
    private Boolean isLocked;

}
