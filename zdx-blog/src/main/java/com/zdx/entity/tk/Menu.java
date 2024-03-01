package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_menu
 */
@TableName(value ="tk_menu")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户菜单实体")
public class Menu extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单名")
    private String name;

    @TableField("order_")
    @Schema(description = "排序")
    private Integer order;

    @Schema(description = "父级菜单id")
    private Long parentId;

    @Schema(description = "菜单动作")
    private String action;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(name = "菜单类型")
    private String type;

    @Schema(name = "菜单权限标识")
    private String params;

    @Schema(name = "是否禁用")
    private Boolean isDisabled;

    @Schema(name = "描述")
    private String description;
}