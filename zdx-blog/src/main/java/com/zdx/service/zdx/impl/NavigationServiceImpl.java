package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Navigation;
import com.zdx.enums.NavigationGroupEnum;
import com.zdx.mapper.zdx.NavigationMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.NavigationVo;
import com.zdx.service.zdx.NavigationService;
import com.zdx.utils.MessageUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<NavigationVo> homeGroup() {
        List<Navigation> list = list(new LambdaQueryWrapper<Navigation>().orderByAsc(Navigation::getOrder));
        List<NavigationVo> navigationList = new ArrayList<>();
        for (NavigationGroupEnum groupEnum : NavigationGroupEnum.values()) {
            NavigationVo navigationVo = new NavigationVo();
            navigationVo.setName(MessageUtil.getLocaleMessage(groupEnum));
            List<Navigation> navigations = list.stream().filter(navigation -> navigation.getGroup().equals(groupEnum.name())).toList();
            navigationVo.setList(navigations);
            navigationList.add(navigationVo);
        }
        return navigationList;
    }
}




