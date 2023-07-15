package com.zdx.service.zdx.impl;

import cn.hutool.core.util.ObjUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Talk;
import com.zdx.service.tk.FileService;
import com.zdx.service.us.UserService;
import com.zdx.service.zdx.TalkService;
import com.zdx.mapper.zdx.TalkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_talk】的数据库操作Service实现
* @createDate 2023-07-14 17:23:35
*/
@Service
@RequiredArgsConstructor
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
    implements TalkService{

    private final UserService userService;

    private final FileService fileService;

    @Override
    public IPage<Talk> pageTalk(RequestParams params, Wrapper<Talk> queryWrapper) {
        IPage<Talk> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Talk> talkIPage = page(iPage, queryWrapper);
        for (Talk talk : talkIPage.getRecords()) {
            User user = userService.getById(talk.getUserId());
            if (ObjUtil.isNotNull(user)) {
                talk.setNickname(user.getNickname());
                String avatar = user.getAvatar();
                if (!avatar.startsWith("http")) {
                    avatar = fileService.getFileUrl(avatar);
                }
                talk.setAvatar(avatar);
                List<String> imgsList = JSON.parseArray(talk.getImages(), String.class);
                talk.setImgList(imgsList);
            }
        }
        return talkIPage;
    }
}




