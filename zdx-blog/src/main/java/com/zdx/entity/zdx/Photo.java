package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_photo
 */
@TableName(value ="zdx_photo")
@Data
@EqualsAndHashCode(callSuper = true)
public class Photo extends BaseTimeEntity {

    private Long albumId;

    private String name;

    private String description;

    private String url;


    private static final long serialVersionUID = 1L;
}