package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Map;

@Data
@Schema(description = "文章搜索实体")
public class ArticleSearchVo {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章描述")
    private String description;

    @Schema(description = "展示高亮的字段")
    private Map<String, Object> highlight;
}
