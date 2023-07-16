package com.zdx.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("博客信息实体")
public class BlogInfoVO {

    @ApiModelProperty(value = "文章数量")
    private Long articleCount;

    @ApiModelProperty(value = "分类数量")
    private Long categoryCount;

    @ApiModelProperty(value = "标签数量")
    private Long tagCount;

    @ApiModelProperty(value = "网站访问量")
    private String viewCount;

    @ApiModelProperty(value = "网站配置")
    private SiteConfig siteConfig;
}
