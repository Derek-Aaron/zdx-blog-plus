package com.zdx.model.vo.front;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "文章归档实体")
public class ArticleArchivesVo {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章封面")
    private String cover;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date createTime;
}
