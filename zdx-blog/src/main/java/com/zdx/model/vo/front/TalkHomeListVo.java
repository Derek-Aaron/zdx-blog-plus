package com.zdx.model.vo.front;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("前台说说列表实体")
public class TalkHomeListVo {

    @ApiModelProperty("说说id")
    private Long id;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("说说内容")
    private String content;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;

    @ApiModelProperty("说说图片")
    private List<String> imgList;

    @ApiModelProperty("点赞数")
    private Long likeCount;

    @ApiModelProperty("评论数")
    private Long commentCount;
}
