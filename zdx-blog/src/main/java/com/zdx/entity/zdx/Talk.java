package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_talk
 */
@TableName(value ="zdx_talk")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("说说实体")
public class Talk extends BaseTimeEntity {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("说说内容")
    private String content;

    @ApiModelProperty("说说内容，json格式")
    private String images;

    @ApiModelProperty("说说点赞量")
    private Long likeCount;

    @ApiModelProperty("是否置顶")
    private Boolean isTop;

    @ApiModelProperty("说说状态")
    private Boolean status;

    @Serial
    private static final long serialVersionUID = 1L;
}