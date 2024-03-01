package com.zdx.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "增加相册图片实体")
public class PhotoAddDto {
    @Schema(description = "相册id")
    private String albumId;

    @Schema(description = "图片链接")
    private List<String> photoUrlList;
}
