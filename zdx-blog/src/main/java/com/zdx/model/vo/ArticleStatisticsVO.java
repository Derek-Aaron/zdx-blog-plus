package com.zdx.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "文章贡献统计")
public class ArticleStatisticsVO {

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer count;
}
