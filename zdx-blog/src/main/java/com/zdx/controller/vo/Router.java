package com.zdx.controller.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("路由实体")
public class Router {

    @ApiModelProperty("菜单id")
    private Long id;

    @ApiModelProperty("菜单名")
    private String name;

    @ApiModelProperty("排序")
    private Integer order;

    @ApiModelProperty("父级菜单")
    private Long parentId;

    @ApiModelProperty("动作")
    private String action;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("类型")
    private String type;
}
