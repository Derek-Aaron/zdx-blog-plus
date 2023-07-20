package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_like
 */
@TableName(value ="zdx_like")
@Data
@EqualsAndHashCode(callSuper = true)
public class Like extends BaseEntity {

    private String type;

    private Long typeId;

    private Long userId;

    @Serial
    private static final long serialVersionUID = 1L;
}