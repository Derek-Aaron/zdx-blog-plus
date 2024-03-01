package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_article
 */
@TableName(value ="zdx_article")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章实体")
public class Article extends BaseTimeEntity {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "分类id")
    private Long categoryId;

    @Schema(description = "背景图")
    private String cover;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "文章类型")
    private String type;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "观看数")
    private Long viewCount;

    @Schema(description = "是否回收")
    private Boolean trash;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @Schema(description = "文章状态")
    private String status;

    @Serial
    private static final long serialVersionUID = 1L;
}