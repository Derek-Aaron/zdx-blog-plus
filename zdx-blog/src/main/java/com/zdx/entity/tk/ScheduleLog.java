package com.zdx.entity.tk;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zdx.Constants;
import com.zdx.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;

/**
 * @TableName tk_schedule_log
 */
@TableName(value ="tk_schedule_log")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private Boolean status;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date startTime;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    private Date oldTime;

    private String exceptionInfo;

    private Long jobId;

}