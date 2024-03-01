package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "最新评论实体")
public class RecentCommentVo {

    @Schema(description = "评论id")
    private Long id;

    @Schema(description = "评论人头像")
    private String avatar;

    @Schema(description = "评论人昵称")
    private String nickname;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    @Schema(description = "评论内容")
    private String content;
}
