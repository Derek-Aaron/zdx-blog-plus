package com.zdx.webSocket;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.zdx.ChatRecord;
import com.zdx.model.vo.front.ChatRecordVo;
import com.zdx.model.vo.front.WebsocketMessageVo;
import com.zdx.service.zdx.ChatRecordService;
import com.zdx.utils.IpAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@ServerEndpoint(value = "/ws/chat", configurator = WebSocketServer.ChatConfigurator.class)
public class WebSocketServer {


     private static  ChatRecordService chatRecordService;

     @Autowired
     public void setChatRecordService(ChatRecordService chatRecordService) {
         WebSocketServer.chatRecordService = chatRecordService;
     }

    /**
     * 在线人数
     */
    private static final AtomicInteger ONLINE_NUM = new AtomicInteger();

    /**
     * 用户session
     */
    private static final Map<String, Session> WS_CONNECTIONS = new ConcurrentHashMap<>();

    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {
        public static final String IP = "X-Real-IP";

        @Override
        public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
            try {
                String ipAddress = request.getHeaders().get(IP.toLowerCase()).get(0);
                config.getUserProperties().put(IP, ipAddress);
            } catch (Exception e) {
                config.getUserProperties().put(IP, "未知ip");
            }
        }
    }

    @OnOpen
    public void opOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        // 当前session加入连接
        String ipAddress = endpointConfig.getUserProperties().get(ChatConfigurator.IP).toString();
        WS_CONNECTIONS.put(ipAddress, session);
        // 更新在线人数
        ONLINE_NUM.incrementAndGet();

        ChatRecordVo chatRecordVo = getChatRecordList(ipAddress);
        WebsocketMessageVo messageVo = WebsocketMessageVo.builder().type(1).data(chatRecordVo).build();
        sendMessage(session, JSON.toJSONString(messageVo));
        sendMessage(session, JSON.toJSONString(WebsocketMessageVo.builder().type(0).data(ONLINE_NUM).build()));
    }

    /**
     * 接受到客户端发送的消息后调用
     *
     * @param session 当前会话
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        WebsocketMessageVo messageDTO = JSON.parseObject(message, WebsocketMessageVo.class);
        switch (Objects.requireNonNull(messageDTO.getType())) {
            case 2:
                // 发送消息
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), ChatRecord.class);
                // 过滤html标签
                chatRecord.setContent(chatRecord.getContent());
                chatRecordService.save(chatRecord);
                messageDTO.setData(chatRecord);
                // 广播消息
                broadcastMessage(JSON.toJSONString(messageDTO));
                break;
            case 3:
                Long id = JSON.parseObject(JSON.toJSONString(messageDTO.getData()), Long.class);
                // 撤回消息
                chatRecordService.removeById(id);
                // 广播消息
                broadcastMessage(JSON.toJSONString(messageDTO));
                break;
            case 4:
                // 心跳消息
                messageDTO.setData("pong");
                sendMessage(session, JSON.toJSONString(messageDTO));
            default:
                break;
        }
    }

    /**
     * 连接关闭时调用
     *
     * @param session 当前会话
     */
    @OnClose
    public void onClose(Session session) {
        // 移除会话
        String ipAddress = session.getUserProperties().get(ChatConfigurator.IP).toString();
        WS_CONNECTIONS.remove(ipAddress);
        // 更新在线人数
        ONLINE_NUM.decrementAndGet();
        updateOnlineCount();
    }

    private void sendMessage(Session session, String json) throws IOException {
        if (session != null && session.isOpen()) {
            synchronized (session) {
                session.getBasicRemote().sendText(json);
            }
        }
    }

    /**
     * 群发消息
     *
     * @param message 消息
     */
    private void broadcastMessage(String message) {
        for (Session session : WS_CONNECTIONS.values()) {
            try {
                sendMessage(session, message);
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    /**
     * 更新在线人数
     */
    private void updateOnlineCount() {
        // 获取当前在线人数
        WebsocketMessageVo messageVo = WebsocketMessageVo.builder()
                .type(0)
                .data(ONLINE_NUM)
                .build();
        // 广播消息
        broadcastMessage(JSON.toJSONString(messageVo));
    }

    private ChatRecordVo getChatRecordList(String ipAddress) {
        List<ChatRecord> chatRecords = chatRecordService.list(new LambdaQueryWrapper<ChatRecord>()
                .ge(ChatRecord::getCreateTime, DateUtil.offsetDay(new Date(), -2))
        );
        ChatRecordVo chatRecordVo = new ChatRecordVo();
        chatRecordVo.setChatRecordList(chatRecords);
        chatRecordVo.setIpAddress(ipAddress);
        chatRecordVo.setIpSource(IpAddressUtil.getCityInfo(ipAddress));
        return chatRecordVo;
    }
}
