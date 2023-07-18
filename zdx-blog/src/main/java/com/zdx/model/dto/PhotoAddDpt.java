package com.zdx.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("照片")
public class PhotoAddDpt {

    @ApiModelProperty("相册id")
    @NotBlank
    private String albumId;

    @ApiModelProperty("相册图片链接")
    @NotEmpty
    private List<String> photoUrlList;
}
