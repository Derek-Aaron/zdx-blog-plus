package com.zdx.wx.strategy;

import com.zdx.wx.entity.WxBaseMessage;

import java.util.Map;

public interface WxMessageStrategy {

    String type();

    WxBaseMessage constructMessage(Map<String, String> requestMap);
}
