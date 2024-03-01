package com.zdx.model.vo;

import com.zdx.entity.zdx.Category;
import com.zdx.entity.zdx.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema(description = "文章同步es实体")
public class ArticleEsVo {

    @Schema(description = "文章id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "文章类型")
    private String type;

    @Schema(description = "文章状态")
    private String status;

    @Schema(description = "文章封面")
    private String cover;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "文章分类")
    private Category category;

    @Schema(description = "标签")
    private List<Tag> tagVOList;

    @Schema(description = "观看数")
    private Long viewCount;

    @Schema(description = "点赞数")
    private Long likeCount;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
