package com.zdx.strategy.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.zdx.entity.tk.Dict;
import com.zdx.enums.DictTypeEnum;
import com.zdx.strategy.DictStrategy;
import com.zdx.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class EnumDictStrategyImpl implements DictStrategy {
    @Override
    public DictTypeEnum type() {
        return DictTypeEnum.ENUMS;
    }

    @Override
    public List<Map<String, String>> execute(Dict dict) {
        if (ObjUtil.isNull(dict)) {
            return null;
        }
        if (StrUtil.isNotBlank(dict.getInvoke())) {
            List<Map<String, String>> properties = new ArrayList<>();
            String invoke = dict.getInvoke();
            if (invoke.contains("class")) {
                invoke = invoke.replaceAll("class:", "");
            }
            try {
                Class<?> clazz = Class.forName(invoke);
                for (Object enumConstant : clazz.getEnumConstants()) {
                    if (enumConstant instanceof Enum<?> e) {
                        Map<String, String> map = new HashMap<>();
                        map.put("key", e.name());
                        map.put("value", MessageUtil.getLocaleMessage(e));
                        properties.add(map);
                    }
                }
                return properties;
            } catch (ClassNotFoundException e) {
                log.info("获取类失败：{}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
