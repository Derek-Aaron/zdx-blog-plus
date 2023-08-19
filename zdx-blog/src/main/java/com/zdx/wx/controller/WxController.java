package com.zdx.wx.controller;


import com.zdx.wx.service.WxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/wx")
@Slf4j
public class WxController {

    @Autowired
    private WxService wxService;
    @GetMapping("/")
    public String wx(String signature,String timestamp,String nonce,String echostr) {
        /**
         * 开发者通过检验signature对请求进行校验（下面有校验方式）。若确认此次GET请求来自微信服务器，请原样返回echostr参数内容，则接入生效，成为开发者成功，否则接入失败。加密/校验流程如下：
         * 1）将token、timestamp、nonce三个参数进行字典序排序
         * 2）将三个参数字符串拼接成一个字符串进行sha1加密
         * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         */
        log.info("微信公众号验证消息");
        if (wxService.check(timestamp,nonce,signature)) {
            return echostr;
        }
        return null;
    }

    @PostMapping("/")
    public String ws(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        //处理消息和事件的推送
        Map<String,String> requestMap = wxService.parseRequest(request.getInputStream());
        log.info("公众号事件消息：{}", requestMap);
        return wxService.getResponse(requestMap);
    }
}
