package com.zdx.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("菜单状态")
public class MenuStatic {

    @ApiModelProperty(value = "菜单id", required = true)
    @NotNull
    private Long menuId;

    @ApiModelProperty("菜单状态")
    private Boolean isDisabled;
}
