package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "第三方vo")
public class AuthVo {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "来源")
    private String source;
}
