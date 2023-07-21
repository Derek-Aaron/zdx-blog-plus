package com.zdx.service.us.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Auth;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.us.AuthService;
import com.zdx.mapper.us.AuthMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【us_auth】的数据库操作Service实现
* @createDate 2023-07-21 16:11:37
*/
@Service
public class AuthServiceImpl extends ServiceImpl<AuthMapper, Auth>
    implements AuthService{

    @Override
    public IPage<Auth> adminPage(RequestParams params) {
        LambdaQueryWrapper<Auth> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("username"), Auth::getUsername, params.getParam("username"));
        queryWrapper.eq(params.hasParam("source"), Auth::getSource, params.getParam("source"));
        return page(new Page<>(params.getPage(), params.getLimit()), queryWrapper);
    }

    @Override
    public Auth getAuthBySource(String source) {
        return getOne(new LambdaQueryWrapper<Auth>().eq(Auth::getSource, source).eq(Auth::getIsEnabled, Boolean.FALSE));
    }
}




