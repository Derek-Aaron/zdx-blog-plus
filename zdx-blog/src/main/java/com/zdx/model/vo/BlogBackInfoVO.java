package com.zdx.model.vo;

import com.zdx.entity.zdx.Tag;
import com.zdx.model.vo.front.CategoryCountVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "网站后台信息VO")
public class BlogBackInfoVO {
    /**
     * 访问量
     */
    @Schema(description = "访问量")
    private Long viewCount;

    /**
     * 留言量
     */
    @Schema(description = "留言量")
    private Long messageCount;

    /**
     * 用户量
     */
    @Schema(description = "用户量")
    private Long userCount;

    /**
     * 文章量
     */
    @Schema(description = "文章量")
    private Long articleCount;

    /**
     * 分类统计
     */
    @Schema(description = "分类统计")
    private List<CategoryCountVo> categoryVOList;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<Tag> tagVOList;

    /**
     * 文章贡献统计
     */
    @Schema(description = "文章贡献统计")
    private List<ArticleStatisticsVO> articleStatisticsList;

    /**
     * 文章浏览量排行
     */
    @Schema(description = "文章浏览量排行")
    private List<ArticleRankVO> articleRankVOList;

}
