package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("最新评论实体")
public class RecentCommentVo {

    @ApiModelProperty("评论id")
    private Long id;

    @ApiModelProperty("评论人头像")
    private String avatar;

    @ApiModelProperty("评论人昵称")
    private String nickname;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    @ApiModelProperty("评论内容")
    private String content;
}
