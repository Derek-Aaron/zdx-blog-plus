package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "说说详情实体")
public class TalkHomeInfoVo {

    @Schema(description = "说说id")
    private Long id;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "说说内容")
    private String content;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "说说图片")
    private List<String> imgList;

    @Schema(description = "说说点赞数")
    private Long likeCount;

}
