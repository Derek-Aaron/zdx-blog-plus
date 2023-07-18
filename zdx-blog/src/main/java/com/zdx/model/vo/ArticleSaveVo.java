package com.zdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("文章录入实体")
public class ArticleSaveVo {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty(value = "文章标题", required = true)
    private String title;

    @ApiModelProperty(value = "文章内容", required = true)
    private String content;

    @ApiModelProperty(value = "分类名称", required = true)
    private String  categoryName;

    @ApiModelProperty(value = "文章简介", required = true)
    private String description;

    @ApiModelProperty(value = "文章标签", required = true)
    private List<String> tagNames;

    @ApiModelProperty("文章类型")
    private String type;

    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("文章是否置顶")
    private Boolean isTop;

    @ApiModelProperty("文章是否推荐")
    private Boolean isRecommend;

    @ApiModelProperty("文章状态")
    private String status;
}
