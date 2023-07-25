package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_tag
 */
@TableName(value ="zdx_tag")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("标签实体")
public class Tag extends BaseTimeEntity {

    @ApiModelProperty("标签名")
    private String name;


    @Serial
    private static final long serialVersionUID = 1L;
}