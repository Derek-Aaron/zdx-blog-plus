package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_photo
 */
@TableName(value ="zdx_photo")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("照片实体")
public class Photo extends BaseTimeEntity {

    @ApiModelProperty("相册id")
    private Long albumId;

    @ApiModelProperty("照片名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("链接")
    private String url;


    @Serial
    private static final long serialVersionUID = 1L;
}