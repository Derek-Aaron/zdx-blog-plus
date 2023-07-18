package com.zdx.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel("用户登录实体")
public class UserLogin {

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @Size(min = 6, message = "{zdx.password.min}")
    private String password;

    @ApiModelProperty("记住我")
    private Boolean rememberMe;
}
