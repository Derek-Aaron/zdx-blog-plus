package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "前台评论实体")
public class CommentHomePageVo {

    @Schema(description = "评论id")
    private String id;

    @Schema(description = "评论头像")
    private String avatar;

    @Schema(description = "评论用户id")
    private Long fromUid;

    @Schema(description = "昵称")
    private String fromNickname;

    @Schema(description = "内容")
    private String content;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "评论点赞数")
    private Long likeCount;

    @Schema(description = "回复列表")
    private List<ReplyVo> replyVoList;

    @Schema(description = "回复量")
    private Long replyCount;
}
