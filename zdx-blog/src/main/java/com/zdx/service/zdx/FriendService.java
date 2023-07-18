package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;

/**
* @author zhaodengxuan
* @description 针对表【zdx_friend】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface FriendService extends IService<Friend> {

    /**
     * 后台分页查询友链
     * @param params 查询参数
     * @return 返回
     */
    IPage<Friend> adminPage(RequestParams params);
}
