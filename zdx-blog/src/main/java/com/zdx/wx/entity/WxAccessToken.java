package com.zdx.wx.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxAccessToken {

    private static final ThreadLocal<WxAccessToken> TOKEN_THREAD_LOCAL = new ThreadLocal<>();

    public static final String WX_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}";

    private String token;

    private Long expireTime;

    public WxAccessToken(String token, String expireIn) {
        this.token = token;
        this.expireTime = System.currentTimeMillis() + Long.parseLong(expireIn) * 1000L;
    }

    /**
     * 判断token是否到期
     * @return
     */
    public boolean isExpired(){
        return System.currentTimeMillis() > expireTime;
    }

    public static WxAccessToken getAccessToken() {
        return TOKEN_THREAD_LOCAL.get();
    }

    public static void setAccessToken(WxAccessToken accessToken) {
        TOKEN_THREAD_LOCAL.set(accessToken);
    }
}
