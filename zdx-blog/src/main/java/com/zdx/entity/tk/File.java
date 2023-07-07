package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
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
public class File extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private String md5;

    private String name;

    private Long size;

    private String bucketName;


}