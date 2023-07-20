package com.zdx.event.handle.zdx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zdx.entity.zdx.Comment;
import com.zdx.entity.zdx.Like;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.security.vo.UserSession;
import com.zdx.service.zdx.CommentService;
import com.zdx.service.zdx.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InsertOrDeleteCommentLikeCountEvent implements EventHandle {

    private final LikeService likeService;

    private final CommentService commentService;
    @Override
    public String getKey() {
        return EventObject.Attribute.INSERT_OR_REMOVE_COMMENT_LIKE;
    }

    @Override
    public void invokeEvent(EventObject event) {
        String typeId = event.getSource(String.class);
        UserSession userSession = event.getAttribute(EventObject.Attribute.USER_SESSION, UserSession.class);
        String likeType = event.getAttribute(EventObject.Attribute.INSERT_LIKE_COUNT, String.class);
        Long likeCount = event.getAttribute(EventObject.Attribute.LIKE_COUNT, Long.class);
        String type = event.getAttribute(EventObject.Attribute.LIKE_COUNT_TYPE, String.class);
        Long userId = userSession.getUserId();
        if ("remove".equals(type)) {
            likeService.remove(new LambdaQueryWrapper<Like>()
                    .eq(Like::getType, likeType).eq(Like::getTypeId, typeId).eq(Like::getUserId, userId));
            likeCount = Optional.ofNullable(likeCount).orElse(0L);
            if (likeCount > 0) {
                likeCount--;
                LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(Comment::getLikeCount, likeCount);
                updateWrapper.eq(Comment::getId, typeId);
                commentService.update(updateWrapper);
            }
        }
        if ("insert".equals(type)) {
            Like like = new Like();
            like.setTypeId(Long.valueOf(typeId));
            like.setType(likeType);
            like.setUserId(userId);
            likeService.save(like);
            likeCount = Optional.ofNullable(likeCount).orElse(0L);
            likeCount++;
            LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Comment::getLikeCount, likeCount);
            updateWrapper.eq(Comment::getId, typeId);
            commentService.update(updateWrapper);
        }
    }
}
