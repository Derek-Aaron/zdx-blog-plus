package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_role
 */
@TableName(value ="us_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    public final static String ADMIN_ROLE_ID = "1676828496567656450";

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String display;

    private String description;

}