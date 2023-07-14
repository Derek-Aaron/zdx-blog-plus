package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_photo
 */
@TableName(value ="zdx_photo")
@Data
@EqualsAndHashCode(callSuper = true)
public class Photo extends BaseTimeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long albumId;

    private String name;

    private String description;

    private String url;



}