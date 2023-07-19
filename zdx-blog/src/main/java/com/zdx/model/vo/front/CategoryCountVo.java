package com.zdx.model.vo.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分类文章数量实体")
public class CategoryCountVo {
    @ApiModelProperty("分类id")
    private Long id;

    @ApiModelProperty("分类名")
    private String name;

    @ApiModelProperty("分类文章数量")
    private Long articleCount;
}
