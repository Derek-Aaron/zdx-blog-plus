package com.zdx.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.zdx.Comment;
import com.zdx.entity.zdx.Like;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.event.EventObject;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.CommentService;
import com.zdx.service.zdx.LikeService;
import com.zdx.strategy.LikeStrategy;
import com.zdx.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CommentLikeStrategyImpl implements LikeStrategy {

    private final CommentService commentService;

    private final LikeService likeService;

    private final ApplicationContext applicationContext;
    @Override
    public LikeTypeEnum likeType() {
        return LikeTypeEnum.COMMENT;
    }

    @Override
    public void execute(String typeId) {
        Comment comment = commentService.getOne(new LambdaQueryWrapper<Comment>()
                .select(Comment::getId, Comment::getLikeCount)
                .eq(Comment::getId, typeId)
                .eq(Comment::getIsCheck, Boolean.TRUE)
        );
        Assert.isFalse(ObjUtil.isNull(comment), MessageUtil.getLocaleMessage("zdx.comment.null"));
        Long userId = UserSessionFactory.getUserId();
        long likeCount = likeService.count(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, userId)
                .eq(Like::getTypeId, typeId)
                .eq(Like::getType, likeType().name())
        );
        if (likeCount > 0) {
            setEvent(typeId, comment, "remove");
        } else {
            setEvent(typeId, comment, "insert");
        }
    }

    private void setEvent(String typeId, Comment comment, String type) {
        EventObject event = new EventObject(typeId, EventObject.Attribute.INSERT_OR_REMOVE_COMMENT_LIKE);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(EventObject.Attribute.INSERT_LIKE_COUNT, likeType().name());
        attributes.put(EventObject.Attribute.USER_SESSION, UserSessionFactory.userDetails());
        attributes.put(EventObject.Attribute.LIKE_COUNT, comment.getLikeCount());
        attributes.put(EventObject.Attribute.LIKE_COUNT_TYPE, type);
        event.setAttributes(attributes);
        applicationContext.publishEvent(event);
    }
}
