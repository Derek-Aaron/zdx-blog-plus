package com.zdx.model.vo.front;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录实体")
public class AuthRenderVo {

    @Schema(description = "登录id")
    private String uuid;

    @Schema(description = "链接")
    private String url;
}
