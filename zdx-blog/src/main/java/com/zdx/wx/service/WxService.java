package com.zdx.wx.service;

import com.zdx.wx.entity.WxAccessToken;

import javax.servlet.ServletInputStream;
import java.util.Map;

public interface WxService {
    /**
     * 微信校验
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    boolean check(String timestamp, String nonce, String signature);

    /**
     * 通过流数据返回数据Map
     * @param is 流
     * @return 返回
     */
    Map<String, String> parseRequest(ServletInputStream is);

    /**
     * 相应数据
     * @param requestMap 数据map
     * @return 返回
     */
    String getResponse(Map<String, String> requestMap);

    /**
     * 获取微信Token 对象
     * @return 返回
     */
    WxAccessToken getAccessToken();

}
