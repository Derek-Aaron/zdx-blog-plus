package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;
import com.zdx.model.vo.front.TalkHomeInfoVo;
import com.zdx.model.vo.front.TalkHomeListVo;

import java.util.List;

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

    /**
     * 前台说说列表
     * @param params 请求参数
     * @return 返回
     */
    IPage<TalkHomeListVo> homePage(RequestParams params);

    /**
     * 说说id获取说说详情
     * @param id 说说id
     * @return 返回
     */
    TalkHomeInfoVo homeGetById(String id);

    /**
     * 查询推荐说说
     * @return 接口
     */
    List<String> homeList();
}
