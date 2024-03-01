package com.zdx.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "文章浏览量排行")
public class ArticleRankVO {

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 浏览量
     */
    @Schema(description = "浏览量")
    private Long viewCount;
}
