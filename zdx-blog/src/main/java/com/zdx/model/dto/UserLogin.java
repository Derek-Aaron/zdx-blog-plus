package com.zdx.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Schema(description = "用户登录实体")
public class UserLogin {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 6, message = "{zdx.password.min}")
    private String password;

    @Schema(description = "记住我")
    private Boolean rememberMe;
}
