package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
@Schema(description = "前台用户实体")
public class UserInfoVo {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户登录方式")
    private String loginType;

    @Schema(description = "用户喜欢的文章列表")
    private Set<String> articleLikeSet;

    @Schema(description = "用户喜欢评论id")
    private Set<String> commentLikeSet;

    @Schema(description = "用户系统说说id")
    private Set<String> talkLikeSet;
}
