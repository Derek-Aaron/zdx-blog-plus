package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_tag
 */
@TableName(value ="zdx_tag")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "标签实体")
public class Tag extends BaseTimeEntity {

    @Schema(description = "标签名")
    private String name;


    @Serial
    private static final long serialVersionUID = 1L;
}