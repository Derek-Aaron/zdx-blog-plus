package com.zdx.strategy;

import com.zdx.entity.tk.Dict;
import com.zdx.enums.DictTypeEnum;

import java.util.List;
import java.util.Map;

public interface DictStrategy {

    DictTypeEnum type();

    List<Map<String, String>> execute(Dict dict);
}
