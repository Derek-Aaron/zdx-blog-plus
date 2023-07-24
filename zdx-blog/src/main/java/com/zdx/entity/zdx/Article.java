package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_article
 */
@TableName(value ="zdx_article")
@Data
@EqualsAndHashCode(callSuper = true)
public class Article extends BaseTimeEntity {

    private Long userId;

    private Long categoryId;

    private String cover;

    private String title;

    private String description;

    private String type;

    private Boolean isTop;

    private Long likeCount;

    private Long viewCount;

    private Boolean trash;

    private Boolean isRecommend;

    private String status;

    @Serial
    private static final long serialVersionUID = 1L;
}