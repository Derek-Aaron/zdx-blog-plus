package com.zdx.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "文章录入实体")
public class ArticleSaveVo {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(description = "文章内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String  categoryName;

    @Schema(description = "文章简介", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @Schema(description = "文章标签", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> tagNames;

    @Schema(description = "文章类型")
    private String type;

    @Schema(description = "文章封面")
    private String cover;

    @Schema(description = "文章是否置顶")
    private Boolean isTop;

    @Schema(description = "文章是否推荐")
    private Boolean isRecommend;

    @Schema(description = "文章状态")
    private String status;
}
