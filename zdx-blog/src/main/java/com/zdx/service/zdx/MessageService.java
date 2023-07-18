package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_message】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface MessageService extends IService<Message> {

    /**
     * 分页查询留言数据
     * @param params 参数
     * @return 返回
     */
    IPage<Message> adminPage(RequestParams params);

    /**
     * 批量审核同步留言
     * @param ids 留言id
     * @return 返回
     */
    boolean through(List<String> ids);
}
