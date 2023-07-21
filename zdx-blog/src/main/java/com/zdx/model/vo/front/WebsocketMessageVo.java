package com.zdx.model.vo.front;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebsocketMessageVo {
    /**
     * 类型
     */
    private Integer type;

    /**
     * 数据
     */
    private Object data;
}
