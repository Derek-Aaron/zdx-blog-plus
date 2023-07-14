package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * @TableName zdx_article
 */
@TableName(value ="zdx_article")
@Data
public class Article implements Serializable {
    private Long id;

    private Long userId;

    private Long categoryId;

    private String articleCover;

    private String articleTitle;

    private String articleType;

    private Boolean isTop;

    private Boolean trash;

    private Boolean isRecommend;

    private String status;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}