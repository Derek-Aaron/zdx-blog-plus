package com.zdx.event.handle;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.zdx.Constants;
import com.zdx.cache.CacheTemplate;
import com.zdx.controller.vo.UserProfile;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.security.vo.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshLoginTokenCacheEvent implements EventHandle {

    private final CacheTemplate<String, Object> cacheTemplate;
    @Override
    public String getKey() {
        return EventObject.Attribute.REFRESH_LOGIN_TOKEN_CACHE;
    }

    @Override
    public void invokeEvent(EventObject event) {
        UserProfile userProfile = event.getSource(UserProfile.class);
        UserPrincipal userSession = event.getAttribute(EventObject.Attribute.USER_SESSION, UserPrincipal.class);
        if (ObjUtil.isNotNull(userProfile) && ObjUtil.isNotNull(userSession)) {
            if (StrUtil.isNotBlank(userProfile.getNickname())) {
                userSession.getUser().setNickname(userProfile.getNickname());
            }
            if (StrUtil.isNotBlank(userProfile.getMobile())) {
                userSession.getUser().setMobile(userProfile.getMobile());
            }
            if (StrUtil.isNotBlank(userProfile.getEmail())) {
                userSession.getUser().setEmail(userProfile.getEmail());
            }
            if (StrUtil.isNotBlank(userProfile.getGender())) {
                userSession.getUser().setGender(userProfile.getGender());
            }
            if (StrUtil.isNotBlank(userProfile.getAvatar())) {
                userSession.getUser().setAvatar(userProfile.getAvatar());
            }
            cacheTemplate.put(Constants.LOGIN_TOKEN_KEY + userSession.getPersonId(), userSession, Constants.EXPIRETIME, TimeUnit.MINUTES);
        }
    }
}
