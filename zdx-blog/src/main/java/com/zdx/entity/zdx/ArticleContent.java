package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_article_content
 */
@TableName(value ="zdx_article_content")
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleContent extends BaseEntity {
    private Long id;

    private String content;

    private Long articleId;

    private static final long serialVersionUID = 1L;
}