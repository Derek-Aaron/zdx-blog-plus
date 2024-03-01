package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "分类文章数量实体")
public class CategoryCountVo {
    @Schema(description = "分类id")
    private Long id;

    @Schema(description = "分类名")
    private String name;

    @Schema(description = "分类文章数量")
    private Long articleCount;
}
