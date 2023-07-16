package com.zdx.controller.us;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSONObject;
import com.zdx.Constants;
import com.zdx.annotation.Log;
import com.zdx.controller.dto.RequestParams;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.security.vo.UserAgent;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.*;

@RestController
@Validated
@RequestMapping("/zdx.online")
@RequiredArgsConstructor
@Api(tags = "在线用户管理")
public class OnlineController {

    private final RedisTemplate<String, Object> redisTemplate;


    @GetMapping("/page")
    @ApiOperation("查询在线登录用户")
    public Result<Map<String, Object>> page(RequestParams params) {
        List<Object> objects = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(Constants.LOGIN_TOKEN_KEY + "**");
        if (ObjUtil.isNotNull(keys) && !keys.isEmpty()) {
            for (String key : keys) {
                objects.add(redisTemplate.opsForValue().get(key));
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
    @ApiOperation("退出用户登录状态")
    public Result<String> out(@PathVariable @NotBlank String id) {
        return Boolean.TRUE.equals(redisTemplate.delete(Constants.LOGIN_TOKEN_KEY + id)) ? Result.success() : Result.error();
    }

}
