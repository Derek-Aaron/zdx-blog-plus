package com.zdx.strategy.impl;

import com.zdx.entity.tk.Dict;
import com.zdx.enums.DictTypeEnum;
import com.zdx.strategy.DictStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class PropertiesDictStrategyImpl implements DictStrategy {
    @Override
    public DictTypeEnum type() {
        return DictTypeEnum.PROPERTIES;
    }

    @Override
    public List<Map<String, String>> execute(Dict dict) {
        String invoke = dict.getInvoke();
        try {
            List<Map<String, String>> data = new ArrayList<>();
            if (invoke.startsWith("resource")) {
                InputStream is = this.getClass().getClassLoader().getResourceAsStream(invoke.replaceAll("resource:", ""));
                Properties properties = new Properties();
                properties.load(is);
                for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("key", entry.getKey().toString());
                    map.put("value", entry.getValue().toString());
                    data.add(map);
                }
            }
            return data;
        } catch (IOException e) {
            log.error("加载properties文件异常：{}", e.getMessage(), e);
           return null;
        }
    }
}
