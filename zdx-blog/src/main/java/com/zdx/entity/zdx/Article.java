package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_article
 */
@TableName(value ="zdx_article")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文章实体")
public class Article extends BaseTimeEntity {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("背景图")
    private String cover;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("文章类型")
    private String type;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("点赞数")
    private Long likeCount;

    @ApiModelProperty("观看数")
    private Long viewCount;

    @ApiModelProperty("是否回收")
    private Boolean trash;

    @ApiModelProperty("是否推荐")
    private Boolean isRecommend;

    @ApiModelProperty("文章状态")
    private String status;

    @Serial
    private static final long serialVersionUID = 1L;
}