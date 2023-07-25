package com.zdx.service.tk.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.entity.tk.Dict;
import com.zdx.enums.DictTypeEnum;
import com.zdx.mapper.tk.DictMapper;
import com.zdx.service.tk.DictService;
import com.zdx.strategy.context.StrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    private final StrategyContext strategyContext;

    @Override
    @Cacheable(cacheNames = Constants.DICT_KEY, key = "#a0")
    public Object getDictByKey(String key) {
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
    @CacheEvict(cacheNames = Constants.DICT_KEY, key = "#a0.key")
    public Boolean saveDict(Dict dict) {
        List<Map<String, String>> properties = getProperties(dict);
        dict.setProperties(JSON.toJSONString(properties));
        return saveOrUpdate(dict);
    }


    public List<Map<String, String>> getProperties(Dict dict) {
        List<Map<String, String>> properties = strategyContext.executeDict(DictTypeEnum.valueOf(dict.getType()), dict);
        if (ObjUtil.isNull(properties)) {
            properties = new ArrayList<>();
        }
        return properties;
    }
}




