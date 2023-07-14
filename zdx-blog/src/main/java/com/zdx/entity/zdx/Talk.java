package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_talk
 */
@TableName(value ="zdx_talk")
@Data
public class Talk implements Serializable {
    private Long id;

    private Long userId;

    private String content;

    private String images;

    private Boolean isTop;

    private Boolean status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}