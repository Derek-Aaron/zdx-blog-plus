package com.zdx.model.vo.front;


import com.zdx.entity.zdx.Photo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("相册图片实体")
public class PhotoInfoVo {

    private String albumName;

    private List<Photo> photoVOList;
}
