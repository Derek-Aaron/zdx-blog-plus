package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_album
 */
@TableName(value ="zdx_album")
@Data
@EqualsAndHashCode(callSuper = true)
public class Album extends BaseTimeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String albumName;

    private String albumCover;

    private String description;

    private Boolean status;

    @TableField(exist = false)
    private Long photoCount;

}