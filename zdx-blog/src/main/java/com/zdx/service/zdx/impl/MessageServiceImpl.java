package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Message;
import com.zdx.service.zdx.MessageService;
import com.zdx.mapper.zdx.MessageMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_message】的数据库操作Service实现
* @createDate 2023-07-14 17:23:35
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

}




