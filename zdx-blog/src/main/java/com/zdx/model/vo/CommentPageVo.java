package com.zdx.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("后台评论列表实体")
public class CommentPageVo {
    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("评论人名称")
    private String fromName;

    @ApiModelProperty("回复人名称")
    private String toName;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("审核是否通过")
    private Boolean isCheck;

    @ApiModelProperty("评论类型")
    private String commentType;
}
