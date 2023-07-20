package com.zdx.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.zdx.Like;
import com.zdx.entity.zdx.Talk;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.event.EventObject;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.LikeService;
import com.zdx.service.zdx.TalkService;
import com.zdx.strategy.LikeStrategy;
import com.zdx.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TalkLikeStrategyImpl implements LikeStrategy {

    private final TalkService talkService;

    private final LikeService likeService;

    private final ApplicationContext applicationContext;
    @Override
    public LikeTypeEnum likeType() {
        return LikeTypeEnum.TALK;
    }

    @Override
    public void execute(String typeId) {
        Talk talk = talkService.getOne(new LambdaQueryWrapper<Talk>()
                .select(Talk::getId, Talk::getLikeCount)
                .eq(Talk::getStatus, Boolean.TRUE)
                .eq(Talk::getId, typeId)
        );
        Assert.isFalse(ObjUtil.isNull(talk), MessageUtil.getLocaleMessage("zdx.talk.null"));
        Long userId = UserSessionFactory.getUserId();
        long likeCount = likeService.count(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, userId)
                .eq(Like::getTypeId, typeId)
                .eq(Like::getType, likeType().name())
        );
        if (likeCount > 0) {
            setEvent(typeId, talk, "remove");
        } else {
            setEvent(typeId, talk, "insert");
        }
    }

    private void setEvent(String typeId, Talk talk, String type) {
        EventObject event = new EventObject(typeId, EventObject.Attribute.INSERT_OR_REMOVE_TALK_LIKE);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(EventObject.Attribute.INSERT_LIKE_COUNT, likeType().name());
        attributes.put(EventObject.Attribute.USER_SESSION, UserSessionFactory.userDetails());
        attributes.put(EventObject.Attribute.LIKE_COUNT, talk.getLikeCount());
        attributes.put(EventObject.Attribute.LIKE_COUNT_TYPE, type);
        event.setAttributes(attributes);
        applicationContext.publishEvent(event);
    }
}
