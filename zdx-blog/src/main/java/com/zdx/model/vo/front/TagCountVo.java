package com.zdx.model.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("标签文章数量实体")
public class TagCountVo {

    @ApiModelProperty("标签id")
    private Long id;

    @ApiModelProperty("标签名")
    private String name;

    @ApiModelProperty("标签文章数量")
    private Long articleCount;
}
