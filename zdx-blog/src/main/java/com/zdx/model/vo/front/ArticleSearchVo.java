package com.zdx.model.vo.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("文章搜索实体")
public class ArticleSearchVo {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章描述")
    private String description;
}
