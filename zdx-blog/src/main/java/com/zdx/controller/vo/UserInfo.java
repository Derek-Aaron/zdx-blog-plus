package com.zdx.controller.vo;

import com.zdx.entity.us.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@Data
@ApiModel("用户信息实体")
public class UserInfo {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户角色")
    private List<Role> roles;

    @ApiModelProperty("用户权限")
    private List<String> permissions;
}
