package com.zdx.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "路由实体")
public class Router {

    @Schema(description = "菜单id")
    private Long id;

    @Schema(description = "菜单名")
    private String name;

    @Schema(description = "排序")
    private Integer order;

    @Schema(description = "父级菜单")
    private Long parentId;

    @Schema(description = "动作")
    private String action;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "类型")
    private String type;
}
