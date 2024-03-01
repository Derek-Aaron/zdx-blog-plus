package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_article_content
 */
@TableName(value ="zdx_article_content")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "博客内容")
public class ArticleContent extends BaseEntity {

    @Schema(description = "内容")
    private String content;

    @Schema(description = "博客id")
    private Long articleId;

    @Serial
    private static final long serialVersionUID = 1L;
}