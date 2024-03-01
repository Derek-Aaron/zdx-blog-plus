package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @Tabledescription tk_config
 */
@TableName(value ="tk_config")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "配置实体")
public class Config extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "配置名")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "配置值")
    private String value;


}