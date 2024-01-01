package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Navigation;
import com.zdx.enums.NavigationGroupEnum;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.NavigationService;
import com.zdx.mapper.zdx.NavigationMapper;
import com.zdx.utils.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author zhaodengxuan
* @description 针对表【zdx_navigation(导航表)】的数据库操作Service实现
* @createDate 2023-12-31 20:59:17
*/
@Service
public class NavigationServiceImpl extends ServiceImpl<NavigationMapper, Navigation>
    implements NavigationService{

    @Override
    public IPage<Navigation> adminPage(RequestParams params) {
        LambdaQueryWrapper<Navigation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("group"), Navigation::getGroup, params.getParam("group"));
        queryWrapper.like(params.hasParam("name"), Navigation::getName, params.getParam("name"));
        IPage<Navigation> iPage = new Page<>(params.getPage(), params.getLimit());
        return page(iPage, queryWrapper);
    }

    @Override
    public Map<String, List<Navigation>> homeGroup() {
        Map<String, List<Navigation>> map = list().stream().collect(Collectors.groupingBy(Navigation::getGroup, Collectors.toList()));
        Map<String, List<Navigation>> resultMap = new HashMap<>();
        for (Map.Entry<String, List<Navigation>> entry : map.entrySet()) {
            resultMap.put(MessageUtil.getLocaleMessage(NavigationGroupEnum.valueOf(entry.getKey())), entry.getValue());
        }
        return resultMap;
    }
}




