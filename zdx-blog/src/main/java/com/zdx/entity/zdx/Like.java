package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_like
 */
@TableName(value ="zdx_like")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "点赞关联实体")
public class Like extends BaseEntity {

    @Schema(description = "点赞类型")
    private String type;

    @Schema(description = "类型id")
    private Long typeId;

    @Schema(description = "用户id")
    private Long userId;

    @Serial
    private static final long serialVersionUID = 1L;
}