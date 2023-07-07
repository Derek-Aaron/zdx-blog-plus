package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_menu
 */
@TableName(value ="tk_menu")
@Data
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    @TableField("order_")
    private Integer order;

    private Long parentId;

    private String action;

    private String icon;

    private String type;

    private String params;

    private Boolean isDisabled;

    private String description;
}