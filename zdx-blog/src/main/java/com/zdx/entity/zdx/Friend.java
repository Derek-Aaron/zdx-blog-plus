package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_friend
 */
@TableName(value ="zdx_friend")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("友链实体")
public class Friend extends BaseTimeEntity {

    @ApiModelProperty("友链名称")
    private String name;

    @ApiModelProperty("颜色")
    private String color;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("链接")
    private String url;

    @ApiModelProperty("说明")
    private String introduction;

    private static final long serialVersionUID = 1L;
}