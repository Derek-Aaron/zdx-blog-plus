package com.zdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("增加相册图片实体")
public class PhotoAddDto {
    @ApiModelProperty("相册id")
    private String albumId;

    @ApiModelProperty("图片链接")
    private List<String> photoUrlList;
}
