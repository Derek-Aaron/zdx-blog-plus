package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "文章上下篇")
public class ArticlePaginationVO {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章封面图片")
    private String cover;
}
