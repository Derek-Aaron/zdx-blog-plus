package com.zdx.wx.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@XStreamAlias("xml")
public class WxTextMessage extends WxBaseMessage{
    @XStreamAlias("Content")
    private String content;

    public WxTextMessage(Map<String,String> requestMap, String content) {
        super(requestMap);
        this.setMsgType("text");
        this.content = content;
    }
}
