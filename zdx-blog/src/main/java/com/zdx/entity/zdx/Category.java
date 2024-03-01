package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_category
 */
@TableName(value ="zdx_category")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "分类实体")
public class Category extends BaseTimeEntity {

    @Schema(description = "分类名")
    private String name;

    @Serial
    private static final long serialVersionUID = 1L;
}