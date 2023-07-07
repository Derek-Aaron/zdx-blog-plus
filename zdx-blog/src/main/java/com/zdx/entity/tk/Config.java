package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @TableName tk_config
 */
@TableName(value ="tk_config")
@Data
@EqualsAndHashCode(callSuper = true)
public class Config extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String remark;

    private String type;

    private String value;


}