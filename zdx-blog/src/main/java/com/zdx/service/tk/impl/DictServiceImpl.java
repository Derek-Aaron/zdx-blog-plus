package com.zdx.service.tk.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.dict.DictHandle;
import com.zdx.entity.tk.Dict;
import com.zdx.mapper.tk.DictMapper;
import com.zdx.service.tk.DictService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
* @author zhaodengxuan
* @description 针对表【tk_dict】的数据库操作Service实现
* @createDate 2023-07-07 21:00:30
*/
@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
    implements DictService{

    private final ApplicationContext applicationContext;

    @Override
    public Dict getDictByKey(String key) {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getKey, key);
        Dict dict = getOne(queryWrapper);
        if (ObjUtil.isNotNull(dict) && StrUtil.isNotBlank(dict.getProperties())) {
            JSONArray properties = JSON.parseArray(dict.getProperties());
            dict.setData(properties);
        }
        return dict;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDict(Dict dict) {
        List<Map<String, String>> properties = getProperties(dict);
        dict.setProperties(JSON.toJSONString(properties));
        return saveOrUpdate(dict);
    }


    public List<Map<String, String>> getProperties(Dict dict) {
        Collection<DictHandle> dictHandles = applicationContext.getBeansOfType(DictHandle.class).values();
        if (!dictHandles.isEmpty()) {
            for (DictHandle dictHandle : dictHandles) {
                if (dictHandle.type().equals(dict.getType())) {
                    return dictHandle.getDictProperties(dict);
                }
            }
        }
        return new ArrayList<>();
    }
}




