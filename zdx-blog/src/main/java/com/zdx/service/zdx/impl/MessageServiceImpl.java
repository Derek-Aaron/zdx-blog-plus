package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Message;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.MessageService;
import com.zdx.mapper.zdx.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_message】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{

    @Override
    public IPage<Message> adminPage(RequestParams params) {
        IPage<Message> iPage = new Page<>(params.getPage(), params.getLimit());
        return page(iPage, new LambdaQueryWrapper<Message>()
                .like(params.hasParam("nickname"), Message::getNickname, params.getParam("nickname"))
        );
    }

    @Override
    public boolean through(List<String> ids) {
        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Message::getIsCheck, Boolean.TRUE);
        updateWrapper.in(Message::getId, ids);
        return update(updateWrapper);
    }
}




