package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_tag
 */
@TableName(value ="zdx_tag")
@Data
public class Tag implements Serializable {
    private Long id;

    private String name;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}