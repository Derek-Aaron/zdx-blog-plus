package com.zdx.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("相册列表")
public class AlbumPhotoCountVo {

    @ApiModelProperty("相册id")
    private Long id;

    @ApiModelProperty("相册名")
    private String name;

    @ApiModelProperty("相册封面")
    private String cover;

    @ApiModelProperty("相册描述")
    private String description;

    @ApiModelProperty("相册状态")
    private Boolean status;

    @ApiModelProperty("相册图片大小")
    private Long photoCount;
}
