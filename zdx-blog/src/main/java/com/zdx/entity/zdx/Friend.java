package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_friend
 */
@TableName(value ="zdx_friend")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "友链实体")
public class Friend extends BaseTimeEntity {

    @Schema(description = "友链名称")
    private String name;

    @Schema(description = "颜色")
    private String color;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "链接")
    private String url;

    @Schema(description = "说明")
    private String introduction;

    private static final long serialVersionUID = 1L;
}