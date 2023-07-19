package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("博客推荐实体")
public class ArticleRecommendVo {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("文章标题")
    private String title;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("文章创建时间")
    private Date createTime;
}
