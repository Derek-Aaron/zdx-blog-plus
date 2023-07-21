package com.zdx.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import com.zdx.service.us.AuthService;
import com.zdx.strategy.AuthStrategy;
import com.zdx.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGiteeRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiteeAuthStrategyImpl implements AuthStrategy {

    private final AuthService authService;
    @Override
    public AuthSourceEnum source() {
        return AuthSourceEnum.GITEE;
    }

    @Override
    public AuthRequest execute(String source, String callbackUrl) {
        Auth auth = authService.getOne(new LambdaQueryWrapper<Auth>()
                .eq(Auth::getIsEnabled, Boolean.FALSE)
                .eq(Auth::getSource, source));
        Assert.isFalse(ObjUtil.isNull(auth), MessageUtil.getLocaleMessage("zdx.auth.null"));
        return new AuthGiteeRequest(AuthConfig.builder()
                .clientId(auth.getClientId())
                .clientSecret(auth.getSecret())
                .redirectUri(callbackUrl)
                .build());
    }
}
