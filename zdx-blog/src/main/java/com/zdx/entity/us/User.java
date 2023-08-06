package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.io.Serial;

/**
 * @TableName us_user
 */
@TableName(value ="us_user")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户实体")
public class User extends BaseTimeEntity {

    /**
     * 演示用户id
     */
    public static  final String PRESENTATION_USER_ID  = "1688074869341224962";
    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @JsonIgnore
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty(value = "邮箱", required = true)
    private String email;

    @ApiModelProperty(value = "手机号", required = true)
    private String mobile;

    @ApiModelProperty("性别")
    private String gender;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("唯一标识")
    private String personId;

    @ApiModelProperty("是否禁用")
    private Boolean isDisabled;

    @ApiModelProperty("是否锁定")
    private Boolean isLocked;

    @ApiModelProperty("描述")
    private String description;


}