package com.zdx.model.vo;

import com.zdx.entity.us.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Data
@Schema(description = "用户信息实体")
public class UserInfo {

    @Schema(description = "用户id")
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户角色")
    private List<Role> roles;

    @Schema(description = "用户权限")
    private List<String> permissions;
}
