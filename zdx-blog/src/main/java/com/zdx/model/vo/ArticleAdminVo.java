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

    private Long viewCount;

    private Long likesCount;

    private Boolean isTop;

    private Boolean isRecommend;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;
}
