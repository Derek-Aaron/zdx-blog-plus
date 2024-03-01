package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_acl
 */
@TableName(value ="us_acl")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限实体")
public class Acl extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主体")
    private String subject;

    @Schema(description = "资源")
    private String resource;

    @Schema(description = "权限标识")
    private String params;

    @Schema(description = "描述")
    private String description;


}