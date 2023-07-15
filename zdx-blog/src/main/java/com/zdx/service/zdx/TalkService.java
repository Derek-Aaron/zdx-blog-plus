package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Talk;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【zdx_talk】的数据库操作Service
* @createDate 2023-07-14 17:23:35
*/
public interface TalkService extends IService<Talk> {

    /**
     * 分页查询说说
     * @param params  参数
     * @param queryWrapper 条件
     * @return 返回
     */
    IPage<Talk> pageTalk(RequestParams params, Wrapper<Talk> queryWrapper);
}
