package com.zdx.model.vo.front;


import com.zdx.entity.zdx.Photo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("相册图片实体")
public class PhotoInfoVo {

    @ApiModelProperty("相册名")
    private String albumName;

    @ApiModelProperty("相册封面")
    private String albumCover;

    @ApiModelProperty("相册图片列表")
    private List<Photo> photoVOList;
}
