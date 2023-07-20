package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("前台博客详情实体")
public class ArticleHomeInfoVo {

    @ApiModelProperty("博客id")
    private Long id;

    @ApiModelProperty("博客标题")
    private String title;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date updateTime;

    @ApiModelProperty("观看数")
    private Long viewCount;

    @ApiModelProperty("分类")
    private Category category;

    @ApiModelProperty("封面")
    private String cover;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("标签")
    private List<Tag> tagVOList;

    @ApiModelProperty("博客点赞数")
    private Long likeCount;

    @ApiModelProperty("上一篇博客")
    private ArticlePaginationVO lastArticle;

    @ApiModelProperty("下一篇博客")
    private ArticlePaginationVO nextArticle;
}
