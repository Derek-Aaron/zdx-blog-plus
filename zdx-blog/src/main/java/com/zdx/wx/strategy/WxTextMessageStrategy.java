package com.zdx.wx.strategy;

import com.zdx.wx.entity.WxBaseMessage;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class WxTextMessageStrategy implements WxMessageStrategy {
    @Override
    public String type() {
        return "text";
    }

    @Override
    public WxBaseMessage constructMessage(Map<String, String> requestMap) {
        return null;
    }
}
