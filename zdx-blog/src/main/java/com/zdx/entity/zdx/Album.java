package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_album
 */
@TableName(value ="zdx_album")
@Data
@EqualsAndHashCode(callSuper = true)
public class Album extends BaseTimeEntity {

    @ApiModelProperty("相册名")
    private String name;

    @ApiModelProperty("相册封面")
    private String cover;

    @ApiModelProperty("相册描述")
    private String description;

    @ApiModelProperty("相册状态")
    private Boolean status;


    @Serial
    private static final long serialVersionUID = 1L;
}