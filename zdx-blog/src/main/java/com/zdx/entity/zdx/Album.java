package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_album
 */
@TableName(value ="zdx_album")
@Data
@EqualsAndHashCode(callSuper = true)
public class Album extends BaseTimeEntity {

    private String name;

    private String cover;

    private String description;

    private Boolean status;


    private static final long serialVersionUID = 1L;
}