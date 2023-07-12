package com.zdx.aspect;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdx.annotation.Encrypt;
import com.zdx.utils.RsaUtil;
import com.zdx.utils.ServletUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

@Component
@Aspect
public class EncryptAspect {

    @Pointcut("@annotation(com.zdx.annotation.Encrypt)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature methodSignature && methodSignature.getMethod().isAnnotationPresent(Encrypt.class)) {
            Object[] args = pjp.getArgs();
            HttpServletRequest request = null;
            String encrypt = "";
            int index = 0;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest servletRequest) {
                    request = servletRequest;
                }
            }
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
            for (int i = 0; i < annotations.length; i++) {
                for (Annotation ann : annotations[i]) {
                    if (ann instanceof Encrypt) {
                        index = i;
                        break;
                    }
                }
            }
            if (ObjUtil.isNull(request)) {
                request = ServletUtils.getRequest();
            }
            if (request.getMethod().equals("get") || request.getMethod().equals("GET")) {
                encrypt = request.getParameter("encrypt");
            }
            if (request.getMethod().equals("post") || request.getMethod().equals("POST")) {
                ServletInputStream is = request.getInputStream();
                JSONObject json = JSONObject.parseObject(is.readAllBytes());
                if (ObjUtil.isNotNull(json)) {
                    encrypt = json.getString("encrypt");
                }
            }
            String decrypt = RsaUtil.decrypt(encrypt);
            if (StrUtil.isNotBlank(decrypt)) {
               args[index] = JSON.parseObject(decrypt, args[index].getClass());
            }
        }
        return pjp.proceed(pjp.getArgs());
    }
}
