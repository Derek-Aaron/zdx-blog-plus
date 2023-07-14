package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Friend;
import com.zdx.service.zdx.FriendService;
import com.zdx.mapper.zdx.FriendMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_friend】的数据库操作Service实现
* @createDate 2023-07-14 17:23:35
*/
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend>
    implements FriendService{

}




