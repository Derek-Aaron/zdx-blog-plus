package com.zdx.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
@Schema(description = "更改用户信息")
public class UserStatus {

    @Schema(description = "用户id")
    @NotNull
    private Long userId;

    @Schema(description = "类型(disabled)禁用(locked)锁定")
    @NotBlank
    private String type;

    @Schema(description = "禁用")
    private Boolean isDisabled;

    @Schema(description = "锁定")
    private Boolean isLocked;

}
