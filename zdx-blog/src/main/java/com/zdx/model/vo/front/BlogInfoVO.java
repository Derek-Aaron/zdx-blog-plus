package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "博客信息实体")
public class BlogInfoVO {

    /**
     * 文章数量
     */
    @Schema(description = "文章数量")
    private Long articleCount;

    /**
     * 分类数量
     */
    @Schema(description = "分类数量")
    private Long categoryCount;

    /**
     * 标签数量
     */
    @Schema(description = "标签数量")
    private Long tagCount;

    /**
     * 网站访问量
     */
    @Schema(description = "网站访问量")
    private String viewCount;

    /**
     * 网站配置
     */
    @Schema(description = "网站配置")
    private SiteConfig siteConfig;
}
