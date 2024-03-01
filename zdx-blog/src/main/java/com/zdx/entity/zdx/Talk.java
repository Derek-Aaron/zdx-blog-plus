package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_talk
 */
@TableName(value ="zdx_talk")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "说说实体")
public class Talk extends BaseTimeEntity {

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "说说内容")
    private String content;

    @Schema(description = "说说内容，json格式")
    private String images;

    @Schema(description = "说说点赞量")
    private Long likeCount;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "说说状态")
    private Boolean status;

    @Serial
    private static final long serialVersionUID = 1L;
}