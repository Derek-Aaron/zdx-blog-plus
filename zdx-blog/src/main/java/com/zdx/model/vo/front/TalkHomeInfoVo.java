package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("说说详情实体")
public class TalkHomeInfoVo {

    @ApiModelProperty("说说id")
    private Long id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("说说内容")
    private String content;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("说说图片")
    private List<String> imgList;

    @ApiModelProperty("说说点赞数")
    private Long likeCount;

}
