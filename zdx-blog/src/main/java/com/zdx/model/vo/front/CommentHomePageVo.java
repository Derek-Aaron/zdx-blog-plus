package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("前台评论实体")
public class CommentHomePageVo {

    @ApiModelProperty("评论id")
    private String id;

    @ApiModelProperty("评论头像")
    private String avatar;

    @ApiModelProperty(value = "评论用户id")
    private Long fromUid;

    @ApiModelProperty("昵称")
    private String fromNickname;

    @ApiModelProperty("内容")
    private String content;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("评论点赞数")
    private Long likeCount;

    @ApiModelProperty("回复列表")
    private List<ReplyVo> replyVoList;

    @ApiModelProperty("回复量")
    private Long replyCount;
}
