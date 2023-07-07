package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_acl
 */
@TableName(value ="us_acl")
@Data
@EqualsAndHashCode(callSuper = true)
public class Acl extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String subject;

    private String resource;

    private String params;

    private String description;


}