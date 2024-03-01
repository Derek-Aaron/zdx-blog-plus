package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "前台博客详情实体")
public class ArticleHomeInfoVo {

    @Schema(description = "博客id")
    private Long id;

    @Schema(description = "博客标题")
    private String title;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date updateTime;

    @Schema(description = "观看数")
    private Long viewCount;

    @Schema(description = "分类")
    private Category category;

    @Schema(description = "封面")
    private String cover;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "标签")
    private List<Tag> tagVOList;

    @Schema(description = "博客点赞数")
    private Long likeCount;

    @Schema(description = "上一篇博客")
    private ArticlePaginationVO lastArticle;

    @Schema(description = "下一篇博客")
    private ArticlePaginationVO nextArticle;
}
