package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_user
 */
@TableName(value ="us_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    @JsonIgnore
    private String password;

    private String nickname;

    private String avatar;

    private String email;

    private String mobile;

    private String gender;

    @TableField(fill = FieldFill.INSERT)
    private String personId;

    private Boolean isDisabled;

    private Boolean isLocked;

    private String description;


}