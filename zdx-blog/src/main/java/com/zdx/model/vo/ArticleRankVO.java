package com.zdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "文章浏览量排行")
public class ArticleRankVO {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Long viewCount;
}
