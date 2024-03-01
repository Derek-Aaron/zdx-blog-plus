package com.zdx.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(description = "菜单状态")
public class MenuStatic {

    @Schema(description = "菜单id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long menuId;

    @Schema(description = "菜单状态")
    private Boolean isDisabled;
}
