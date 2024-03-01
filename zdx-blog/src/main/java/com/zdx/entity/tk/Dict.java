package com.zdx.entity.tk;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_dict
 */
@TableName(value ="tk_dict")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "数据字典")
public class Dict extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "数据字典名")
    private String name;

    @Schema(description = "key")
    @TableField("key_")
    private String key;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "内容")
    private String properties;

    @Schema(description = "扩展类")
    private String invoke;

    @Schema(description = "是否禁用")
    private Boolean isDisabled;

    @TableField(exist = false)
    @Schema(description = "内容json")
    private JSONArray data;

}