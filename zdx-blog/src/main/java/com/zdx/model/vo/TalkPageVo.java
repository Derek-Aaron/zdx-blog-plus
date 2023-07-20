package com.zdx.model.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("说说实体")
public class TalkPageVo {

    @ApiModelProperty("说说id")
    private Long id;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("发布状态")
    private Boolean status;

    @ApiModelProperty("说说图片")
    private List<String> imgList;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;
}
