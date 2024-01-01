package com.zdx.model.vo;


import lombok.Data;
import me.zhyd.oauth.request.AuthRequest;

import java.util.HashMap;
import java.util.Map;

@Data
public class AuthRequestVo {
    private String state;

    private AuthRequest authRequest;

    private Map<String, Object> attributes = new HashMap<>(16);


    public AuthRequestVo setAttribute(String name, Object value) {
        this.attributes.put(name, value);
        return this;
    }
}
