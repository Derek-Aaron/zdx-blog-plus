package com.zdx.model.vo.front;


import com.zdx.entity.zdx.ChatRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "聊天记录实体")
public class ChatRecordVo {

    /**
     * 聊天记录
     */
    @Schema(description = "聊天记录")
    private List<ChatRecord> chatRecordList;

    /**
     * ip地址
     */
    @Schema(description = "ip地址")
    private String ipAddress;

    /**
     * ip来源
     */
    @Schema(description = "ip来源")
    private String ipSource;
}
