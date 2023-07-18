package com.zdx.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("重置密码")
public class ResetPwd {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("老密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true)
    private String newPassword;
}
