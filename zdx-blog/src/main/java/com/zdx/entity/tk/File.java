package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "文件实体")
public class File extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "文件md5")
    private String md5;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件存放路径")
    private String bucketName;


}