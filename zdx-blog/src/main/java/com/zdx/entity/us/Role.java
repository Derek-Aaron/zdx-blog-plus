package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_role
 */
@TableName(value ="us_role")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色实体")
public class Role extends BaseEntity {

    public final static String ADMIN_ROLE_ID = "1676828496567656450";

    public final static String BLOG_USE_ID = "1682272591209517057";

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色")
    private String name;

    @Schema(description = "角色名")
    private String display;

    @Schema(description = "角色描述")
    private String description;

}