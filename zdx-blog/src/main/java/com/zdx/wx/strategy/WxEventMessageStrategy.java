package com.zdx.wx.strategy;

import com.zdx.wx.entity.WxBaseMessage;

import java.util.Map;

public class WxEventMessageStrategy implements WxMessageStrategy {
    @Override
    public String type() {
        return "event";
    }

    @Override
    public WxBaseMessage constructMessage(Map<String, String> requestMap) {
        return null;
    }
}
