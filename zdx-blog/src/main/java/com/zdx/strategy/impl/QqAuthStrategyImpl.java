package com.zdx.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import com.zdx.strategy.AuthStrategy;
import com.zdx.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QqAuthStrategyImpl implements AuthStrategy {


    @Override
    public AuthSourceEnum source() {
        return AuthSourceEnum.QQ;
    }

    @Override
    public AuthRequest execute(Auth auth) {
        Assert.isFalse(ObjUtil.isNull(auth), MessageUtil.getLocaleMessage("zdx.auth.null"));
        return new AuthQqRequest(AuthConfig.builder()
                .clientId(auth.getClientId())
                .clientSecret(auth.getSecret())
                .redirectUri(auth.getCallback())
                .build());
    }
}
