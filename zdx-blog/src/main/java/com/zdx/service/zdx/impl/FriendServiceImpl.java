package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Friend;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.FriendService;
import com.zdx.mapper.zdx.FriendMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_friend】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

    @Override
    public IPage<Friend> adminPage(RequestParams params) {
        IPage<Friend> iPage = new Page<>(params.getPage(), params.getLimit());
        return page(iPage, new LambdaQueryWrapper<Friend>()
                .like(params.hasParam("name"), Friend::getName, params.getParam("name"))
                .orderByAsc(Friend::getCreateTime)
        );
    }
}




