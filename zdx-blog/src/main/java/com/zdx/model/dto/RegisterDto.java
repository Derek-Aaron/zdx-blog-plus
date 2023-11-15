package com.zdx.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("注册实体")
public class RegisterDto {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String code;

    @ApiModelProperty("邮箱")
    private String email;
}
