package com.zdx.controller.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("重置密码")
public class ResetPwd {

    @ApiModelProperty("老密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank
    private String newPassword;
}
