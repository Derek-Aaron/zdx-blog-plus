package com.zdx.model.vo.front;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

@Data
@ApiModel("前台用户实体")
public class UserInfoVo {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户登录方式")
    private String loginType;

    @ApiModelProperty("用户喜欢的文章列表")
    private Set<String> articleLikeSet;

    @ApiModelProperty("用户喜欢评论id")
    private Set<String> commentLikeSet;

    @ApiModelProperty("用户系统说说id")
    private Set<String> talkLikeSet;
}
