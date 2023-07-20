package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("评论回复实体")
public class ReplyVo {


    @ApiModelProperty("回复id")
    private Long id;

    @ApiModelProperty("父级评论id")
    private Long  parentId;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("评论用户昵称")
    private String fromNickname;

    @ApiModelProperty("评论用户id")
    private Long fromUid;

    @ApiModelProperty("被评论用户id")
    private Long toUid;

    @ApiModelProperty("被评论用户昵称")
    private String toNickname;

    @ApiModelProperty("评论内容")
    private String content;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("点赞数")
    private Long likeCount;
}
