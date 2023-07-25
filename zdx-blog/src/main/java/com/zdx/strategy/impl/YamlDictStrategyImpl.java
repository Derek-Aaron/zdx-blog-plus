package com.zdx.strategy.impl;

import com.zdx.entity.tk.Dict;
import com.zdx.enums.DictTypeEnum;
import com.zdx.strategy.DictStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class YamlDictStrategyImpl implements DictStrategy {
    @Override
    public DictTypeEnum type() {
        return DictTypeEnum.YAML;
    }

    @Override
    public List<Map<String, String>> execute(Dict dict) {
        String invoke = dict.getInvoke();
        List<Map<String, String>> data = new ArrayList<>();
        if (invoke.startsWith("resource")) {
            Yaml yaml = new Yaml();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(invoke.replaceAll("resource:", ""));
            Map<String, Object> ret = yaml.load(is);
            for (Map.Entry<String, Object> entry : ret.entrySet()) {
                Map<String, String> map = new HashMap<>();
                map.put("key", entry.getKey());
                map.put("value", entry.getValue().toString());
                data.add(map);
            }
        }
        return data;
    }
}
