package com.zdx.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "后台展示文章实体")
public class ArticleAdminVo {

    @Schema(description = "文章编号")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章分类名")
    private String categoryName;

    @Schema(description = "观看数")
    private Long viewCount;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "是否推荐")
    private Boolean isRecommend;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "创建时间")
    private Date createTime;
}
