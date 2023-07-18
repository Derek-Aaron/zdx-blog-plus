package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_category
 */
@TableName(value ="zdx_category")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("分类实体")
public class Category extends BaseTimeEntity {

    @ApiModelProperty("分类名")
    private String name;

    private static final long serialVersionUID = 1L;
}