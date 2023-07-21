package com.zdx.model.vo.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("登录实体")
public class AuthRenderVo {

    @ApiModelProperty("登录id")
    private String uuid;

    @ApiModelProperty("链接")
    private String url;
}
