package com.zdx.model.vo.front;


import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Schema(description = "文章分类标签实体")
@Data
public class CategoryTagArticleVo {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章封面")
    private String cover;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章创建时间")
    private Date createTime;

    @Schema(description = "文章分类")
    private Category category;

    @Schema(description = "文章标签")
    private List<Tag> tagVOList;
}
