package com.zdx.controller;


import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/captcha")
@Api(tags = "验证码")
public class CaptchaController {
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/get")
    @ApiOperation("获取验证码")
    public Object captchaGet(@RequestBody CaptchaVO captchaVO, HttpServletRequest request) {
        captchaVO.setBrowserInfo(getRemoteId(request));
        return captchaService.get(captchaVO);
    }

    @PostMapping("/check")
    @ApiOperation("校验验证码")
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    public static String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }
    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StrUtil.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StrUtil.trim(ipList[0]);
        }
        return null;
    }
}
