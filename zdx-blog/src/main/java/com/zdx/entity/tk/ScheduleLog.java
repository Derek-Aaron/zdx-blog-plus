package com.zdx.entity.tk;

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
 * @TableName tk_schedule_log
 */
@TableName(value ="tk_schedule_log")
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "定时任务日志实体")
public class ScheduleLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "状态")
    private Boolean status;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "开始时间")
    private Date startTime;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @Schema(description = "结束时间")
    private Date oldTime;

    @Schema(description = "异常信息")
    private String exceptionInfo;

    @Schema(description = "任务id")
    private Long jobId;

}