package com.zdx.entity.tk;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_dict
 */
@TableName(value ="tk_dict")
@Data
@EqualsAndHashCode(callSuper = true)
public class Dict extends BaseTimeEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    @TableField("key_")
    private String key;

    private String type;

    private String properties;

    private String invoke;

    private Boolean isDisabled;

    @TableField(exist = false)
    private JSONArray data;

}