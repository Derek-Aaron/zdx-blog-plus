package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_navigation
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="zdx_navigation")
@Data
@Schema(description = "导航")
public class Navigation extends BaseTimeEntity {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "图片")
    private String avatar;

    @Schema(description = "连接")
    private String url;

    @TableField("group_")
    @Schema(description = "分组")
    private String group;

    @TableField("order_")
    @Schema(description = "排序")
    private Integer order;

    @Schema(description = "描述")
    private String introduction;

    @Serial
    private static final long serialVersionUID = 1L;
}