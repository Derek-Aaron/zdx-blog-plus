package com.zdx.model.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("音乐实体")
public class MusicVo {

    @ApiModelProperty("音乐名称")
    private String name;

    @ApiModelProperty("作者")
    private String artist;

    @ApiModelProperty("音乐链接")
    private String url;

    @ApiModelProperty("音乐封面")
    private String cover;

    @ApiModelProperty("歌词")
    private String lrc;

    @ApiModelProperty("主题")
    private String theme;
}
