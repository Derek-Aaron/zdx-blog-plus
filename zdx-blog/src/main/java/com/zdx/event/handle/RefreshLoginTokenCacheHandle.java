package com.zdx.event.handle;


import cn.hutool.core.util.ObjUtil;
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
public class RefreshLoginTokenCacheHandle implements EventHandle {

    private final CacheTemplate<String, Object> cacheTemplate;
    @Override
    public String getKey() {
        return EventObject.Attribute.REFRESH_LOGIN_TOKEN_CACHE;
    }

    @Override
    public void invokeEvent(EventObject event) {
        UserProfile userProfile = event.getSource(UserProfile.class);
        UserPrincipal userSession = event.getAttribute("userSession", UserPrincipal.class);
        if (ObjUtil.isNotNull(userProfile) && ObjUtil.isNotNull(userSession)) {
            userSession.getUser().setNickname(userProfile.getNickname());
            userSession.getUser().setMobile(userProfile.getMobile());
            userSession.getUser().setEmail(userProfile.getEmail());
            userSession.getUser().setGender(userProfile.getGender());
            cacheTemplate.put(Constants.LOGIN_TOKEN_KEY + userSession.getPersonId(), userSession, Constants.EXPIRETIME, TimeUnit.MINUTES);
        }
    }
}
