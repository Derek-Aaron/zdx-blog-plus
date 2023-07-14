package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_friend
 */
@TableName(value ="zdx_friend")
@Data
@EqualsAndHashCode(callSuper = true)
public class Friend extends BaseTimeEntity {

    private String name;

    private String color;

    private String avatar;

    private String url;

    private String introduction;


    @Serial
    private static final long serialVersionUID = 1L;
}