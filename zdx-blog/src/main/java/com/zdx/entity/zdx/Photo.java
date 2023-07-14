package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_photo
 */
@TableName(value ="zdx_photo")
@Data
public class Photo implements Serializable {
    private Long id;

    private Long albumId;

    private String name;

    private String description;

    private String url;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}