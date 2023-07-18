package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;

/**
* @author zhaodengxuan
* @description 针对表【zdx_talk】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface TalkService extends IService<Talk> {

    /**
     * 分页查询说说
     * @param params 请求参数
     * @return 返回
     */
    IPage<TalkPageVo> adminPage(RequestParams params);
}
