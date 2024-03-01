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
@Schema(description = "前台文章列表实体")
public class ArticleHomeVo {

    @Schema(description = "文章实体")
    private Long id;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "文章创建时间")
    private Date createTime;

    @JsonFormat(pattern = "文章封面")
    private String cover;

    @Schema(description = "文章标签")
    private List<Tag> tagVOList;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章描述")
    private String description;

    @Schema(description = "文章分类")
    private Category category;
}
