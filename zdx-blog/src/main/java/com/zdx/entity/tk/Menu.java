package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_menu
 */
@TableName(value ="tk_menu")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户菜单实体")
public class Menu extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜单名")
    private String name;

    @TableField("order_")
    @ApiModelProperty("排序")
    private Integer order;

    @ApiModelProperty("父级菜单id")
    private Long parentId;

    @ApiModelProperty("菜单动作")
    private String action;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("菜单类型")
    private String type;

    @ApiModelProperty("菜单权限标识")
    private String params;

    @ApiModelProperty("是否禁用")
    private Boolean isDisabled;

    @ApiModelProperty("描述")
    private String description;
}