package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "博客推荐实体")
public class ArticleRecommendVo {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章封面")
    private String cover;

    @Schema(description = "文章标题")
    private String title;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "文章创建时间")
    private Date createTime;
}
