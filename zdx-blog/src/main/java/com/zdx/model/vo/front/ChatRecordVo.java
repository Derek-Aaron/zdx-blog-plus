package com.zdx.model.vo.front;


import com.zdx.entity.zdx.ChatRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("聊天记录实体")
public class ChatRecordVo {

    /**
     * 聊天记录
     */
    @ApiModelProperty(value = "聊天记录")
    private List<ChatRecord> chatRecordList;

    /**
     * ip地址
     */
    @ApiModelProperty(value = "ip地址")
    private String ipAddress;

    /**
     * ip来源
     */
    @ApiModelProperty(value = "ip来源")
    private String ipSource;
}
