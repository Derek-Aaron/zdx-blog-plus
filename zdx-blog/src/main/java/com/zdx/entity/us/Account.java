package com.zdx.entity.us;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName us_account
 */
@TableName(value ="us_account")
@Data
@EqualsAndHashCode(callSuper = true)
public class Account extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private Boolean isDisabled;

    private Boolean isLocked;

    private Long userId;

}