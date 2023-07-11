package com.zdx.aspect;


import com.alibaba.fastjson.JSON;
import com.zdx.annotation.Encrypt;
import com.zdx.controller.dto.RequestParams;
import com.zdx.utils.RsaUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

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
                for (Object arg : args) {
                    if (arg instanceof RequestParams params) {
                        String decrypt = RsaUtil.decrypt(params.getEncrypt());
                        params.setParams(JSON.parseObject(decrypt).getInnerMap());
                    }
                }
        }
        return pjp.proceed();
    }
}
