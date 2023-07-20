package com.zdx.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEntity implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("实体id")
    private Long id;
}
