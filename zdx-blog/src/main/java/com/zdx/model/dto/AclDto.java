package com.zdx.model.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("权限")
public class AclDto {

    @NotEmpty
    @ApiModelProperty("主体")
    private List<String> subjects;

    @NotBlank
    @ApiModelProperty("类型")
    private String type;

    @NotEmpty
    @ApiModelProperty("资源")
    private List<String> resources;
}
