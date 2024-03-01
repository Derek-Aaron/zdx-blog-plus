package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_photo
 */
@TableName(value ="zdx_photo")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "照片实体")
public class Photo extends BaseTimeEntity {

    @Schema(description = "相册id")
    private Long albumId;

    @Schema(description = "照片名称")
    private String name;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "链接")
    private String url;


    @Serial
    private static final long serialVersionUID = 1L;
}