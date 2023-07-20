package com.zdx.entity.tk;

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
 * @TableName tk_schedule_log
 */
@TableName(value ="tk_schedule_log")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("定时任务日志实体")
public class ScheduleLog extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("状态")
    private Boolean status;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("开始时间")
    private Date startTime;

    @JsonFormat(pattern = Constants.FORMAT_STRING)
    @ApiModelProperty("结束时间")
    private Date oldTime;

    @ApiModelProperty("异常信息")
    private String exceptionInfo;

    @ApiModelProperty("任务id")
    private Long jobId;

}