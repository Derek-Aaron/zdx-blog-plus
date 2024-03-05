package com.zdx.aspect;


import com.zdx.event.EventObject;
import com.zdx.security.UserSessionFactory;
import com.zdx.utils.IpAddressUtil;
import com.zdx.utils.ServletUtils;
import com.zdx.utils.UserAgentUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final ApplicationContext applicationContext;


    @Pointcut("@annotation(com.zdx.annotation.Log)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object proceed = pjp.proceed();
        stopWatch.stop();
        EventObject event = new EventObject(pjp, EventObject.Attribute.LOG_SAVE);
        HttpServletRequest request = ServletUtils.getRequest();
        event.setAttribute(EventObject.Attribute.IP, IpAddressUtil.getIp(request))
                .setAttribute(EventObject.Attribute.USERAGENT, UserAgentUtils.parseOsAndBrowser(request))
                .setAttribute(EventObject.Attribute.USER_SESSION, UserSessionFactory.userDetails())
                .setAttribute(EventObject.Attribute.REQUEST_URI, request.getRequestURI())
                .setAttribute(EventObject.Attribute.STOP_WATCH, stopWatch);
        applicationContext.publishEvent(event);
        return proceed;
    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void doAfterThrowing(JoinPoint point, Throwable e) {
        EventObject event = new EventObject(point, EventObject.Attribute.LOG_SAVE);
        HttpServletRequest request = ServletUtils.getRequest();
        event.setAttribute(EventObject.Attribute.USER_SESSION, UserSessionFactory.userDetails())
                .setAttribute(EventObject.Attribute.IP, IpAddressUtil.getIp(request))
                .setAttribute(EventObject.Attribute.USERAGENT, UserAgentUtils.parseOsAndBrowser(request))
                .setAttribute(EventObject.Attribute.REQUEST_URI, request.getRequestURI())
                .setAttribute(EventObject.Attribute.THROWABLE, e);
        applicationContext.publishEvent(event);
    }
}
