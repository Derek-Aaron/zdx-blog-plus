package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_album
 */
@TableName(value ="zdx_album")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "相册")
public class Album extends BaseTimeEntity {

    @Schema(description = "相册名")
    private String name;

    @Schema(description = "相册封面")
    private String cover;

    @Schema(description = "相册描述")
    private String description;

    @Schema(description = "相册状态")
    private Boolean status;


    @Serial
    private static final long serialVersionUID = 1L;
}