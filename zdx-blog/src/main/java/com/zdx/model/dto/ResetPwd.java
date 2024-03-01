package com.zdx.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "重置密码")
public class ResetPwd {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "老密码")
    private String oldPassword;

    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
