package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_category
 */
@TableName(value ="zdx_category")
@Data
@EqualsAndHashCode(callSuper = true)
public class Category extends BaseTimeEntity {

    private String name;

    @Serial
    private static final long serialVersionUID = 1L;
}