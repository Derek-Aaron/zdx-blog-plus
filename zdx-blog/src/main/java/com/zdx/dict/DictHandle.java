package com.zdx.dict;

import com.zdx.entity.tk.Dict;

import java.util.List;
import java.util.Map;

public interface DictHandle {

    String type();


    List<Map<String, String>> getDictProperties(Dict dict);
}
