package com.zdx.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "说说实体")
public class TalkPageVo {

    @Schema(description = "说说id")
    private Long id;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "发布状态")
    private Boolean status;

    @Schema(description = "说说图片")
    private List<String> imgList;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "创建时间")
    private Date createTime;
}
