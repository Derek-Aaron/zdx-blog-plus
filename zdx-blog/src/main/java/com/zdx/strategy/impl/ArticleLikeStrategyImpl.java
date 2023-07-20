package com.zdx.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.Like;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.event.EventObject;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.tk.RedisService;
import com.zdx.service.zdx.ArticleService;
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
public class ArticleLikeStrategyImpl implements LikeStrategy {

    private final ArticleService articleService;

    private final RedisService redisService;

    private final LikeService likeService;

    private final ApplicationContext applicationContext;

    @Override
    public LikeTypeEnum likeType() {
        return LikeTypeEnum.ARTICLE;
    }

    @Override
    public void execute(String typeId) {
        Article article = articleService.getOne(new LambdaQueryWrapper<Article>()
                .select(Article::getLikeCount, Article::getId)
                .eq(Article::getTrash, Boolean.FALSE)
                .eq(Article::getId, typeId));
        Assert.isFalse(ObjUtil.isNull(article), MessageUtil.getLocaleMessage("zdx.article.null"));
        Long userId = UserSessionFactory.getUserId();
        long likeCount = likeService.count(new LambdaQueryWrapper<Like>()
                .eq(Like::getUserId, userId)
                .eq(Like::getTypeId, typeId)
                .eq(Like::getType, likeType().name())
        );
        if (likeCount > 0) {
            setEvent(typeId, article, "remove");

        } else {
            setEvent(typeId, article, "insert");
        }
    }

    private void setEvent(String typeId, Article article, String type) {
        EventObject event = new EventObject(typeId, EventObject.Attribute.INSERT_OR_REMOVE_ARTICLE_LIKE);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(EventObject.Attribute.INSERT_LIKE_COUNT, likeType().name());
        attributes.put(EventObject.Attribute.USER_SESSION, UserSessionFactory.userDetails());
        attributes.put(EventObject.Attribute.LIKE_COUNT, article.getLikeCount());
        attributes.put(EventObject.Attribute.LIKE_COUNT_TYPE, type);
        event.setAttributes(attributes);
        applicationContext.publishEvent(event);
    }
}
