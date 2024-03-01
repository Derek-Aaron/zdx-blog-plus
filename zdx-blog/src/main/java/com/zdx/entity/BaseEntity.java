package com.zdx.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseEntity implements Serializable {


    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "实体id")
    private Long id;
}
