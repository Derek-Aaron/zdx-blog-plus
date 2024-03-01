package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "评论回复实体")
public class ReplyVo {


    @Schema(description = "回复id")
    private Long id;

    @Schema(description = "父级评论id")
    private Long  parentId;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "评论用户昵称")
    private String fromNickname;

    @Schema(description = "评论用户id")
    private Long fromUid;

    @Schema(description = "被评论用户id")
    private Long toUid;

    @Schema(description = "被评论用户昵称")
    private String toNickname;

    @Schema(description = "评论内容")
    private String content;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "点赞数")
    private Long likeCount;
}
