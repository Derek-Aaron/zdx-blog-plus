package com.zdx.model.vo.front;


import com.zdx.entity.zdx.Photo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "相册图片实体")
public class PhotoInfoVo {

    @Schema(description = "相册名")
    private String albumName;

    @Schema(description = "相册封面")
    private String albumCover;

    @Schema(description = "相册图片列表")
    private List<Photo> photoVOList;
}
