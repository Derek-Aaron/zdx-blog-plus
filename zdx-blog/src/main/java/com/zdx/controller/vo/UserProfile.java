package com.zdx.controller.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel("用户信息实体")
public class UserProfile {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    @NotBlank
    private String nickname;

    @ApiModelProperty("邮箱")
    @NotBlank
    private String email;

    @ApiModelProperty("手机号")
    @NotBlank
    private String mobile;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("性别")
    private String gender;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("角色")
    private String roleNames;
}
