package com.zdx.model.vo.front;


import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("文章分类标签实体")
@Data
public class CategoryTagArticleVo {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章创建时间")
    private Date createTime;

    @ApiModelProperty("文章分类")
    private Category category;

    @ApiModelProperty("文章标签")
    private List<Tag> tagVOList;
}
