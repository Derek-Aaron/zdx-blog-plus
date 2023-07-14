package com.zdx.service.zdx;

import com.zdx.entity.zdx.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_message】的数据库操作Service
* @createDate 2023-07-14 17:23:35
*/
public interface MessageService extends IService<Message> {


    /**
     * 批量审核通过留言
     * @param ids 留言id
     * @return 成功
     */
    boolean through(List<String> ids);
}
