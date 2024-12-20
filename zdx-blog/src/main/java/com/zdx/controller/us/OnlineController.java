package com.zdx.controller.us;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSONObject;
import com.zdx.Constants;
import com.zdx.annotation.Log;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.security.vo.UserAgent;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.tk.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Validated
@RequestMapping("/zdx.online")
@RequiredArgsConstructor
@Tag(name = "在线用户管理")
public class OnlineController {

    private final RedisService redisService;


    @GetMapping("/page")
    @Operation(summary = "查询在线登录用户")
    public Result<Map<String, Object>> page(RequestParams params) {
        List<Object> objects = new ArrayList<>();
        Collection<String> keys = redisService.getKeys(Constants.LOGIN_TOKEN_KEY + "**");
        if (ObjUtil.isNotNull(keys) && !keys.isEmpty()) {
            for (String key : keys) {
                objects.add(redisService.getObject(key));
            }
        }
        List<UserSession> userSessions = new ArrayList<>();
        List<UserAgent> userAgents = new ArrayList<>();
        if (ObjUtil.isNotNull(objects) && !objects.isEmpty()) {
            List<Object> list = objects.stream()
                    .map(o -> {
                        if (o instanceof UserSession userSession) {
                            return userSession;
                        }
                        if (o instanceof JSONObject json) {
                            return json.toJavaObject(UserPrincipal.class);
                        }
                        if (o instanceof Map<?, ?> map) {
                            return BeanUtil.fillBeanWithMap(map, new UserPrincipal(), true);
                        }
                        return o;
                    }).toList();
            for (Object o : list) {
                if (o instanceof UserSession userSession) {
                    userSessions.add(userSession);
                }
            }
            for (UserSession userSession : userSessions) {
                UserAgent userAgent = ((UserPrincipal) userSession).getUserAgent();
                userAgents.add(userAgent);
            }
        }
        if (params.hasParam("username")) {
            userAgents = userAgents.stream().filter(userAgent -> userAgent.getUsername().contains(params.getParam("username", String.class))).toList();
        }
        Map<String, Object> result = new HashMap<>();
        result.put("total", userAgents.size());
        userAgents = userAgents.stream().skip(((long) (params.getPage() - 1) * params.getLimit())).limit(params.getLimit()).toList();
        result.put("records", userAgents);
        return Result.success(result);
    }

    @GetMapping("/out/{id}")
    @Log(type = LogEventEnum.DELETE, desc = "退出用户登录")
    @Operation(summary = "退出用户登录状态")
    @PreAuthorize("hasAuthority('zdx:online:exit')")
    public Result<String> out(@PathVariable @NotBlank String id) {
        return Boolean.TRUE.equals(redisService.deleteObject(Constants.LOGIN_TOKEN_KEY + id)) ? Result.success() : Result.error();
    }

}
