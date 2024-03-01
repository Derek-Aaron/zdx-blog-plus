package com.zdx.event.handle;


import cn.hutool.core.util.StrUtil;
import com.zdx.entity.us.Log;
import com.zdx.enums.LogEventEnum;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.security.vo.UserSession;
import com.zdx.service.us.LogService;
import com.zdx.utils.IpAddressUtil;
import com.zdx.utils.UserAgentUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginSuccessEvent implements EventHandle {

    private final LogService logService;
    @Override
    public String getKey() {
        return EventObject.Attribute.LOGINLOG;
    }

    @Override
    public void invokeEvent(EventObject event) {
        UserSession userSession = event.getSource(UserSession.class);
        HttpServletRequest request = event.getAttribute(EventObject.Attribute.REQUEST, HttpServletRequest.class);

        Log log = new Log();
        log.setEvent(LogEventEnum.LOGIN.name());
        log.setUserId(userSession.getUserId());
        log.setUsername(userSession.getUsername());
        String ip = IpAddressUtil.getIp(request);
        log.setIp(ip);
        log.setSource(IpAddressUtil.getCityInfo(ip));
        Map<String, String> map = UserAgentUtils.parseOsAndBrowser(request);
        log.setOs(map.get("os"));
        log.setBrowser(map.get("browser"));
        log.setStatus(Boolean.TRUE);
        log.setContent(StrUtil.format("【{}】登录系统", userSession.getUsername()));
        logService.saveOrUpdate(log);
    }
}
