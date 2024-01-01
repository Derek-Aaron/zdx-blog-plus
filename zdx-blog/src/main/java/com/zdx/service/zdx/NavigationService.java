package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Navigation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;

import java.util.List;
import java.util.Map;

/**
* @author zhaodengxuan
* @description 针对表【zdx_navigation(导航表)】的数据库操作Service
* @createDate 2023-12-31 20:59:17
*/
public interface NavigationService extends IService<Navigation> {


    /**
     * 分页查询导航
     * @param params 参数
     * @return 返回
     */
    IPage<Navigation> adminPage(RequestParams params);

    /**
     * 前端分组查询
     *
     * @return 前端查询
     */
    Map<String, List<Navigation>> homeGroup();

}
