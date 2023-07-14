package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName zdx_album
 */
@TableName(value ="zdx_album")
@Data
public class Album implements Serializable {
    private Long id;

    private String albumName;

    private String albumCover;

    private String description;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}