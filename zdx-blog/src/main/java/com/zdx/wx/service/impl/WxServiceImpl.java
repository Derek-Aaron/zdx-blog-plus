package com.zdx.wx.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.zdx.service.tk.ConfigService;
import com.zdx.wx.entity.WxAccessToken;
import com.zdx.wx.entity.WxBaseMessage;
import com.zdx.wx.service.WxService;
import com.zdx.wx.strategy.WxMessageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;

import javax.servlet.ServletInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


@Service
@Slf4j
public class WxServiceImpl implements WxService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ApplicationContext applicationContext;

    private static final String TOKEN = "zdxzjy";
    @Override
    public boolean check(String timestamp, String nonce, String signature) {
        String[] strs = new String[]{TOKEN, timestamp, nonce};
        Arrays.sort(strs);
        String str = strs[0] + strs[1] + strs[2];
        String mysig = sha1(str);
        return Objects.requireNonNull(mysig).equalsIgnoreCase(signature);
    }

    @Override
    @SuppressWarnings("all")
    public Map<String, String> parseRequest(ServletInputStream is) {
        Map<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        try {
            //读取输入流获取文档对象
            Document document = reader.read(is);
            //根据文档对象获取根节点
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Element e : elements) {
                map.put(e.getName(), e.getStringValue());
            }
            return map;
        } catch (DocumentException e) {
            log.error("读取xml文件失败{}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getResponse(Map<String, String> requestMap) {
        String msgType = requestMap.get("MsgType");
        WxBaseMessage message = null;
        Map<String, WxMessageStrategy> strategyMap = applicationContext.getBeansOfType(WxMessageStrategy.class);
        for (WxMessageStrategy strategy : strategyMap.values()) {
            if (strategy.type().equals(msgType)) {
                message = strategy.constructMessage(requestMap);
                break;
            }
        }
        if (ObjUtil.isNotNull(message)) {
            String beanXml =  beanXml(message);
            log.info("要发送的消息：{}", beanXml);
            return beanXml;
        }
        return null;
    }

    @Override
    public WxAccessToken getAccessToken() {
        WxAccessToken accessToken = WxAccessToken.getAccessToken();
        if (ObjUtil.isNotNull(accessToken) && accessToken.isExpired()) {
            return accessToken;
        }
        Map<String, String> jsonObject = new HashMap<>();
        jsonObject.put("grant_type", "client_credential");
        jsonObject.put("appid", configService.getConfig("WX_APP_ID", String.class, "wx309b4a3456873a48"));
        jsonObject.put("secret", configService.getConfig("WX_SECRET_ID", String.class, "3ac13567a96845bd28f74925d0d21f85"));
        JSONObject forObject = restTemplate.getForObject(WxAccessToken.WX_TOKEN_URL, JSONObject.class, jsonObject);
        if (ObjUtil.isNotNull(forObject)) {
            if (forObject.containsKey("access_token") && forObject.containsKey("expires_in")) {
                String access_token = forObject.getString("access_token");
                String expires_in = forObject.getString("expires_in");
                accessToken = new WxAccessToken(access_token, expires_in);
                WxAccessToken.setAccessToken(accessToken);
                return accessToken;
            } else {
                log.error("异常:{}", forObject);
                throw new RuntimeException("调用微信token异常");
            }
        }
        return null;
    }


    @SuppressWarnings("all")
    private String beanXml(WxBaseMessage message) {
        XStream xStream = new XStream();
        xStream.processAnnotations(TextMessage.class);
        return xStream.toXML(message);
    }


    private String sha1(String str) {
        try {
            //获取加密对象
            MessageDigest md = MessageDigest.getInstance("sha1");
            //加密
            byte[] digest = md.digest(str.getBytes(StandardCharsets.UTF_8));
            char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
            StringBuilder sb = new StringBuilder();
            //处理加密结果
            for (byte b : digest) {
                sb.append(chars[(b >> 4) & 15]);
                sb.append(chars[b & 15]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("加密失败{}", e.getMessage());
        }
        return null;
    }
}
