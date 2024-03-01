package com.zdx.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户邮箱实体")
public class UserEmailDto {

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "验证码")
    private String code;
}
