package com.zdx.entity.zdx;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zdx.entity.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @TableName zdx_talk
 */
@TableName(value ="zdx_talk")
@Data
@EqualsAndHashCode(callSuper = true)
public class Talk extends BaseTimeEntity {

    private Long userId;

    private String content;

    private String images;

    private Boolean isTop;

    private Boolean status;

    private static final long serialVersionUID = 1L;
}