package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * @TableName tk_file
 */
@TableName(value ="tk_file")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文件实体")
public class File extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("文件md5")
    private String md5;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("文件大小")
    private Long size;

    @ApiModelProperty("文件存放路径")
    private String bucketName;


}