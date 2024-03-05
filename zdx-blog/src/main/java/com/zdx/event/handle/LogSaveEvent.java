package com.zdx.event.handle;


import cn.hutool.core.util.ObjUtil;
import com.zdx.annotation.Log;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.security.vo.UserSession;
import com.zdx.service.us.LogService;
import com.zdx.utils.IpAddressUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class LogSaveEvent implements EventHandle {

    private final LogService logService;
    @Override
    public String getKey() {
        return EventObject.Attribute.LOG_SAVE;
    }

    @Override
    public void invokeEvent(EventObject event) {
        JoinPoint point = event.getSource(JoinPoint.class);
        UserSession userSession = event.getAttribute(EventObject.Attribute.USER_SESSION, UserSession.class);
        String ip = event.getAttribute(EventObject.Attribute.IP, String.class);
        Map<String, String> map = event.getAttribute(EventObject.Attribute.USERAGENT, Map.class);
        String requestURI = event.getAttribute(EventObject.Attribute.REQUEST_URI, String.class);
        StopWatch stopWatch = event.getAttribute(EventObject.Attribute.STOP_WATCH, StopWatch.class);
        Throwable throwable = event.getAttribute(EventObject.Attribute.THROWABLE, Throwable.class);
        if (point.getSignature() instanceof MethodSignature methodSignature) {
            if (methodSignature.getMethod().isAnnotationPresent(Log.class)) {
                Log annotation = methodSignature.getMethod().getAnnotation(Log.class);
                com.zdx.entity.us.Log logDb = new com.zdx.entity.us.Log();
                if (ObjUtil.isNotNull(throwable)) {
                    logDb.setContent(throwable.getMessage());
                    logDb.setStatus(Boolean.FALSE);
                    log.error("用户访问：【{}】, 访问：【{}】-> 异常信息：【{}】", userSession.getUsername(), requestURI, throwable.getMessage(), throwable);
                } else {
                    long time = stopWatch.getTotalTimeMillis();
                    logDb.setContent(annotation.desc() + "运行时长：" + time + "毫秒");
                    logDb.setStatus(Boolean.TRUE);
                }
                logDb.setEvent(annotation.type().name());
                logDb.setUsername(userSession.getUsername());
                logDb.setUserId(userSession.getUserId());
                logDb.setUrl(requestURI);
                logDb.setIp(ip);
                logDb.setSource(IpAddressUtil.getCityInfo(ip));
                logDb.setOs(map.get("os"));
                logDb.setBrowser(map.get("browser"));
                if (annotation.save()) {
                    logService.saveOrUpdate(logDb);
                }
            }
        }
    }
}
