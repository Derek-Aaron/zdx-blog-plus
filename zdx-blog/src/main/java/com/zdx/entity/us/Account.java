package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_account
 */
@TableName(value ="us_account")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户账号实体")
public class Account extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "是否禁用")
    private Boolean isDisabled;

    @Schema(description = "是否锁定")
    private Boolean isLocked;

    @Schema(description = "用户id")
    private Long userId;

}