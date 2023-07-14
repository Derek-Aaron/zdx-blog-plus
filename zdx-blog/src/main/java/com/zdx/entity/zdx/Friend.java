package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_friend
 */
@TableName(value ="zdx_friend")
@Data
public class Friend implements Serializable {
    private Long id;

    private String name;

    private String color;

    private String avatar;

    private String url;

    private String introduction;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}