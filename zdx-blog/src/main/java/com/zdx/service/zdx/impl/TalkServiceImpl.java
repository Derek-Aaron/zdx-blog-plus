package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Talk;
import com.zdx.mapper.us.UserMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;
import com.zdx.service.zdx.TalkService;
import com.zdx.mapper.zdx.TalkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_talk】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
@RequiredArgsConstructor
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
    implements TalkService{

    private final UserMapper userMapper;

    @Override
    public IPage<TalkPageVo> adminPage(RequestParams params) {
        IPage<Talk> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Talk> page = page(iPage, new LambdaQueryWrapper<Talk>()
                .eq(params.hasParam("status"), Talk::getStatus, params.getParam("status", Boolean.class)));
        IPage<TalkPageVo> talkPageVoIPage = new Page<>(params.getPage(), params.getLimit());
        talkPageVoIPage.setPages(page.getPages());
        talkPageVoIPage.setTotal(page.getTotal());
        talkPageVoIPage.setSize(page.getSize());
        talkPageVoIPage.setCurrent(page.getCurrent());
        List<TalkPageVo> talkPageVos = new ArrayList<>();
        for (Talk talk : page.getRecords()) {
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getUsername, User::getNickname, User::getAvatar)
                    .eq(User::getId, talk.getUserId())
            );
            TalkPageVo talkPageVo = BeanUtil.copyProperties(talk, TalkPageVo.class);
            if (ObjUtil.isNotNull(user)) {
                talkPageVo.setNickname(StrUtil.isNotBlank(user.getNickname()) ? user.getNickname() : user.getUsername());
                talkPageVo.setAvatar(user.getAvatar());
            }
            if (StrUtil.isNotBlank(talk.getImages()) && talk.getImages().startsWith("[")) {
                List<String> urls = JSON.parseArray(talk.getImages(), String.class);
                talkPageVo.setImgList(urls);
            }
            talkPageVos.add(talkPageVo);
        }
        talkPageVoIPage.setRecords(talkPageVos);
        return talkPageVoIPage;
    }
}




