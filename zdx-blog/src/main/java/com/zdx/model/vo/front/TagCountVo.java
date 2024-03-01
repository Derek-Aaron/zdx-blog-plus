package com.zdx.model.vo.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "标签文章数量实体")
public class TagCountVo {

    @Schema(description = "标签id")
    private Long id;

    @Schema(description = "标签名")
    private String name;

    @Schema(description = "标签文章数量")
    private Long articleCount;
}
