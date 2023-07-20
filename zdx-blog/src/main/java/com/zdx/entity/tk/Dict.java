package com.zdx.entity.tk;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_dict
 */
@TableName(value ="tk_dict")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("数据字典")
public class Dict extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据字典名")
    private String name;

    @ApiModelProperty("key")
    @TableField("key_")
    private String key;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("内容")
    private String properties;

    @ApiModelProperty("扩展类")
    private String invoke;

    @ApiModelProperty("是否禁用")
    private Boolean isDisabled;

    @TableField(exist = false)
    @ApiModelProperty("内容json")
    private JSONArray data;

}