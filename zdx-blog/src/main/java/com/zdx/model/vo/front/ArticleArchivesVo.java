package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("文章归档实体")
public class ArticleArchivesVo {

    @ApiModelProperty("文章id")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    @ApiModelProperty("文章封面")
    private String cover;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;
}
