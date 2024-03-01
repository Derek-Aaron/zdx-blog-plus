package com.zdx.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "后台评论列表实体")
public class CommentPageVo {
    @Schema(description = "评论id")
    private Long id;

    @Schema(description = "评论人名称")
    private String fromName;

    @Schema(description = "回复人名称")
    private String toName;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "审核是否通过")
    private Boolean isCheck;

    @Schema(description = "评论类型")
    private String commentType;
}
