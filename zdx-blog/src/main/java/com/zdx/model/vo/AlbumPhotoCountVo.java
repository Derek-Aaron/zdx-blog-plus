package com.zdx.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "相册列表")
public class AlbumPhotoCountVo {

    @Schema(description = "相册id")
    private Long id;

    @Schema(description = "相册名")
    private String name;

    @Schema(description = "相册封面")
    private String cover;

    @Schema(description = "相册描述")
    private String description;

    @Schema(description = "相册状态")
    private Boolean status;

    @Schema(description = "相册图片大小")
    private Long photoCount;
}
