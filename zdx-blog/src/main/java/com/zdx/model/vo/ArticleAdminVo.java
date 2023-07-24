package com.zdx.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("后台展示文章实体")
public class ArticleAdminVo {

    @ApiModelProperty("文章编号")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章分类名")
    private String categoryName;

    @ApiModelProperty("观看数")
    private Long viewCount;

    @ApiModelProperty("点赞数")
    private Long likeCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("是否推荐")
    private Boolean isRecommend;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("创建时间")
    private Date createTime;
}
