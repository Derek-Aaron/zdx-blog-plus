package com.zdx.controller.tk;


import cn.hutool.core.util.ObjUtil;
import com.zdx.model.vo.server.Server;
import com.zdx.handle.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Tag(name = "系统管理")
@RequestMapping("/zdx.system")
@RequiredArgsConstructor
public class SystemController {

    private final RedisTemplate<String, Object> redisTemplate;



    @GetMapping("/server")
    @Operation(summary = "获取服务信息")
    public Result<Server> server() throws Exception {
        Server server = new Server();
        server.copyTo();
        return Result.success(server);
    }

    @GetMapping("/cache")
    @Operation(summary = "缓存监控信息")
    public Result<Map<String, Object>> cache() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        if (ObjUtil.isNotNull(commandStats)) {
            commandStats.stringPropertyNames().forEach(key -> {
                Map<String, String> data = new HashMap<>(2);
                String property = commandStats.getProperty(key);
                data.put("name", StringUtils.removeStart(key, "cmdstat_"));
                data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
                pieList.add(data);
            });
        }
        result.put("commandStats", pieList);
        return Result.success(result);
    }
}
