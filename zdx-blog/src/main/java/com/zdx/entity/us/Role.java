package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_role
 */
@TableName(value ="us_role")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("角色实体")
public class Role extends BaseEntity {

    public final static String ADMIN_ROLE_ID = "1676828496567656450";

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色")
    private String name;

    @ApiModelProperty("角色名")
    private String display;

    @ApiModelProperty("角色描述")
    private String description;

}