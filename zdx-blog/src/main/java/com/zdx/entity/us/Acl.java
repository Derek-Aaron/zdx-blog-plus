package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_acl
 */
@TableName(value ="us_acl")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("权限实体")
public class Acl extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主体")
    private String subject;

    @ApiModelProperty("资源")
    private String resource;

    @ApiModelProperty("权限标识")
    private String params;

    @ApiModelProperty("描述")
    private String description;


}