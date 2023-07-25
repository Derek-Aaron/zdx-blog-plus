package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName zdx_like
 */
@TableName(value ="zdx_like")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("点赞关联实体")
public class Like extends BaseEntity {

    @ApiModelProperty("点赞类型")
    private String type;

    @ApiModelProperty("类型id")
    private Long typeId;

    @ApiModelProperty("用户id")
    private Long userId;

    @Serial
    private static final long serialVersionUID = 1L;
}