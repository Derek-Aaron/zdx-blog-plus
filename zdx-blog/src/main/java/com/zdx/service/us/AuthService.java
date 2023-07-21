package com.zdx.service.us;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.us.Auth;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;

/**
* @author zhaodengxuan
* @description 针对表【us_auth】的数据库操作Service
* @createDate 2023-07-21 16:11:37
*/
public interface AuthService extends IService<Auth> {

    /**
     * 后台分页查询登录项
     * @param params 请求参数
     * @return 返回
     */
    IPage<Auth> adminPage(RequestParams params);

    /**
     * 通过来源查询登录项
     * @param source 来源
     * @return 返回
     */
    Auth getAuthBySource(String source);
}
