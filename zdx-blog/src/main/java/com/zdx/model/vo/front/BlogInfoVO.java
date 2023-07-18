package com.zdx.model.vo.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("博客信息实体")
public class BlogInfoVO {

    /**
     * 文章数量
     */
    @ApiModelProperty(value = "文章数量")
    private Long articleCount;

    /**
     * 分类数量
     */
    @ApiModelProperty(value = "分类数量")
    private Long categoryCount;

    /**
     * 标签数量
     */
    @ApiModelProperty(value = "标签数量")
    private Long tagCount;

    /**
     * 网站访问量
     */
    @ApiModelProperty(value = "网站访问量")
    private String viewCount;

    /**
     * 网站配置
     */
    @ApiModelProperty(value = "网站配置")
    private SiteConfig siteConfig;
}
