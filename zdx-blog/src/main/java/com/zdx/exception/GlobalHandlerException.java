package com.zdx.exception;


import com.zdx.handle.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {

    /**
     * 权限异常拦截
     * @param e 异常
     * @param request 请求
     * @return 返回
     */
    @ExceptionHandler(AuthenticationException.class)
    public Result<String> handleAuthenticationException(AuthenticationException e, HttpServletRequest request) {
        log.error("请求地址【{}】,权限校验失败【{}】", request.getRequestURI(), e.getMessage(), e);
        return Result.error(HttpStatus.FORBIDDEN.value(), e.getMessage());
    }

    /**
     * 断言异常拦截
     * @param e 异常
     * @param request 请求
     * @return 返回
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e,
                                                         HttpServletRequest request) {
        log.error("请求地址【{}】,错误信息【{}】", request.getRequestURI(), e.getLocalizedMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 自定义异常拦截
     * @param e 异常
     * @param request 请求
     * @return 返回
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException e,
                                              HttpServletRequest request) {
        log.error("请求地址【{}】,错误信息【{}】", request.getRequestURI(), e.getLocalizedMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 全局异常拦截
     * @param e 异常
     * @param request 请求
     * @return 返回
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleException(RuntimeException e,
                                          HttpServletRequest request) {
        log.error("请求地址【{}】,错误信息【{}】", request.getRequestURI(), e.getLocalizedMessage(), e);
        return Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }

    /**
     * 数据校验异常拦截
     * @param e 异常
     * @param request 请求
     * @return 返回
     */
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, })
    public Result<String> parameterMissingHandler(Exception e, HttpServletRequest request) {
        log.error("请求地址【{}】,错误信息【{}】", request.getRequestURI(), e.getLocalizedMessage(), e);
       if (e instanceof MethodArgumentNotValidException exception) {
           if (!exception.getAllErrors().isEmpty()) {
               String message = exception.getAllErrors().get(0).getDefaultMessage();
               return Result.error(message);
           }
       }
       if (e instanceof ConstraintViolationException exception) {
           return Result.error(exception.getMessage());
       }
        return Result.error();
    }
}
