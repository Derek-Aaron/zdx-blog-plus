package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * @TableName zdx_article
 */
@TableName(value ="zdx_article")
@Data
@EqualsAndHashCode(callSuper = true)
public class Article extends BaseTimeEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long categoryId;

    private String articleCover;

    private String articleTitle;

    private String articleType;

    private Long viewCount;

    private Long likesCount;

    private Boolean isTop;

    private Boolean trash;

    private Boolean isRecommend;

    private String status;

    @TableField(exist = false)
    private String categoryName;

    @TableField(exist = false)
    private List<String> tagNames;

    @TableField(exist = false)
    private String content;


}