package com.zdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户浏览")
public class UserViewVO {

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    private String date;

    /**
     * pv
     */
    @ApiModelProperty(value = "pv")
    private Long pv;

    /**
     * uv
     */
    @ApiModelProperty(value = "uv")
    private Long uv;
}
