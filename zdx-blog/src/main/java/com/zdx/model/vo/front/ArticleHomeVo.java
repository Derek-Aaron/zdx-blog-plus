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
@ApiModel("前台文章列表实体")
public class ArticleHomeVo {

    @ApiModelProperty("文章实体")
    private Long id;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("文章创建时间")
    private Date createTime;

    @JsonFormat(pattern = "文章封面")
    private String cover;

    @ApiModelProperty("文章标签")
    private List<Tag> tagVOList;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章描述")
    private String description;

    @ApiModelProperty("文章分类")
    private Category category;
}
