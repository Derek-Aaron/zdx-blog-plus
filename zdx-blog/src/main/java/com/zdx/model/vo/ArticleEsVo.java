package com.zdx.model.vo;

import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("文章同步es实体")
public class ArticleEsVo {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("文章类型")
    private String type;

    @ApiModelProperty("文章状态")
    private String status;

    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("文章分类")
    private Category category;

    @ApiModelProperty("标签")
    private List<Tag> tagVOList;

    @ApiModelProperty("观看数")
    private Long viewCount;

    @ApiModelProperty("点赞数")
    private Long likeCount;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}
