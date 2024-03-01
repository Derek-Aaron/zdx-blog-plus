package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_user
 */
@TableName(value ="us_user")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户实体")
public class User extends BaseTimeEntity {

    /**
     * 演示用户id
     */
    public static  final String PRESENTATION_USER_ID  = "1688074869341224962";
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobile;

    @Schema(description = "性别")
    private String gender;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "唯一标识")
    private String personId;

    @Schema(description = "是否禁用")
    private Boolean isDisabled;

    @Schema(description = "是否锁定")
    private Boolean isLocked;

    @Schema(description = "描述")
    private String description;


}