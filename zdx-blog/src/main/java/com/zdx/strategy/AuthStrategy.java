package com.zdx.strategy;

import com.google.common.collect.Maps;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import me.zhyd.oauth.request.AuthRequest;

import java.util.Map;

public interface AuthStrategy {

    Map<String, AuthRequest> AUTH_REQUEST_MAP = Maps.newConcurrentMap();


    AuthSourceEnum source();


    AuthRequest execute(Auth auth);
}
