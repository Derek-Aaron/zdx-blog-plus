package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_config
 */
@TableName(value ="tk_config")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("配置实体")
public class Config extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置名")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("配置值")
    private String value;


}