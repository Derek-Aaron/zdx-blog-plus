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
import com.zdx.entity.zdx.Comment;
import com.zdx.entity.zdx.Talk;
import com.zdx.enums.CommentTypeEnum;
import com.zdx.mapper.us.UserMapper;
import com.zdx.mapper.zdx.CommentMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.TalkPageVo;
import com.zdx.model.vo.front.TalkHomeInfoVo;
import com.zdx.model.vo.front.TalkHomeListVo;
import com.zdx.service.zdx.TalkService;
import com.zdx.mapper.zdx.TalkMapper;
import com.zdx.utils.MybatisPlusUtils;
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

    private final CommentMapper commentMapper;

    @Override
    public IPage<TalkPageVo> adminPage(RequestParams params) {
        IPage<Talk> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Talk> page = page(iPage, new LambdaQueryWrapper<Talk>()
                .eq(params.hasParam("status"), Talk::getStatus, params.getParam("status", Boolean.class)));

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
        return MybatisPlusUtils.pageConvert(page, talkPageVos);
    }

    @Override
    public IPage<TalkHomeListVo> homePage(RequestParams params) {
        IPage<Talk> iPage = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Talk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Talk::getId, Talk::getUserId, Talk::getImages, Talk::getLikeCount, Talk::getContent);
        IPage<Talk> page = page(iPage, queryWrapper.eq(Talk::getStatus, Boolean.TRUE));
        List<TalkHomeListVo> talkHomeListVos = new ArrayList<>();
        for (Talk talk : page.getRecords()) {
            TalkHomeListVo talkHomeListVo = BeanUtil.copyProperties(talk, TalkHomeListVo.class);
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getUsername, User::getNickname, User::getAvatar)
                    .eq(User::getId, talk.getUserId())
            );
            if (ObjUtil.isNotNull(user)) {
                talkHomeListVo.setNickname(StrUtil.isNotBlank(user.getNickname()) ? user.getNickname() : user.getUsername());
                talkHomeListVo.setAvatar(user.getAvatar());
            }
            if (StrUtil.isNotBlank(talk.getImages()) && talk.getImages().startsWith("[")) {
                List<String> urls = JSON.parseArray(talk.getImages(), String.class);
                talkHomeListVo.setImgList(urls);
            }
            Long commentCount = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                    .eq(Comment::getCommentType, CommentTypeEnum.TALK.name())
                    .eq(Comment::getTypeId, talk.getId())
            );
            talkHomeListVo.setCommentCount(commentCount);
            talkHomeListVos.add(talkHomeListVo);
        }
        return MybatisPlusUtils.pageConvert(page, talkHomeListVos);
    }

    @Override
    public TalkHomeInfoVo homeGetById(String id) {
        Talk talk = getById(id);
        TalkHomeInfoVo talkHomeInfoVo = null;
        if (ObjUtil.isNotNull(talk)) {
            talkHomeInfoVo = BeanUtil.copyProperties(talk, TalkHomeInfoVo.class);
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getUsername, User::getNickname, User::getAvatar)
                    .eq(User::getId, talk.getUserId())
            );
            if (ObjUtil.isNotNull(user)) {
                talkHomeInfoVo.setNickname(StrUtil.isNotBlank(user.getNickname()) ? user.getNickname() : user.getUsername());
                talkHomeInfoVo.setAvatar(user.getAvatar());
            }
            if (StrUtil.isNotBlank(talk.getImages()) && talk.getImages().startsWith("[")) {
                List<String> urls = JSON.parseArray(talk.getImages(), String.class);
                talkHomeInfoVo.setImgList(urls);
            }
        }
        return talkHomeInfoVo;
    }

    @Override
    public List<String> homeList() {
        LambdaQueryWrapper<Talk> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Talk::getContent);
        queryWrapper.eq(Talk::getStatus, Boolean.TRUE);
        queryWrapper.last("limit 5");
        return list(queryWrapper).stream().map(Talk::getContent).toList();
    }
}




