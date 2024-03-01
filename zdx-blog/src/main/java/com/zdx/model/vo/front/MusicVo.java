package com.zdx.model.vo.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "音乐实体")
public class MusicVo {

    @Schema(description = "音乐名称")
    private String name;

    @Schema(description = "作者")
    private String artist;

    @Schema(description = "音乐链接")
    private String url;

    @Schema(description = "音乐封面")
    private String cover;

    @Schema(description = "歌词")
    private String lrc;

    @Schema(description = "主题")
    private String theme;
}
