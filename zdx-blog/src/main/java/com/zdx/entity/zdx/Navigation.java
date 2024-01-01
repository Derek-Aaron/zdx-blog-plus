package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_navigation
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="zdx_navigation")
@Data
public class Navigation extends BaseTimeEntity {

    private String name;

    private String avatar;

    private String url;

    @TableField("group_")
    private String group;

    private String introduction;

    @Serial
    private static final long serialVersionUID = 1L;
}