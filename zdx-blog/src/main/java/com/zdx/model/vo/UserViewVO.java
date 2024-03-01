package com.zdx.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户浏览")
public class UserViewVO {

    /**
     * 日期
     */
    @Schema(description = "日期")
    private String date;

    /**
     * pv
     */
    @Schema(description = "pv")
    private Long pv;

    /**
     * uv
     */
    @Schema(description = "uv")
    private Long uv;
}
