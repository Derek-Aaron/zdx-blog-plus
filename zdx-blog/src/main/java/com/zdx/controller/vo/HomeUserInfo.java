package com.zdx.controller.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Set;

@ApiModel("博客前台用户实体")
@Data
public class HomeUserInfo {

    private Long id;

    private String avatar;

    private String nickname;

    private String email;

    private String username;

    private String webSite;

    private String intro;

    private String loginType;

    private Set<Object> articleLikeSet;

    private Set<Object> commentLikeSet;

    private Set<Object> talkLikeSet;
}
