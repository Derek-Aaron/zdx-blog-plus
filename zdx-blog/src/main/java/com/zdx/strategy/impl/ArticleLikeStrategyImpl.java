package com.zdx.strategy.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.Constants;
import com.zdx.entity.zdx.Article;
import com.zdx.enums.ArticleLikeTypeEnum;
import com.zdx.enums.ArticleStatusEnum;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.security.UserSessionFactory;
import com.zdx.strategy.LikeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class ArticleLikeStrategyImpl implements LikeStrategy {

    private final ArticleMapper articleMapper;

    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public ArticleLikeTypeEnum getType() {
        return ArticleLikeTypeEnum.ARTICLE;
    }

    @Override
    public void like(String typeId) {
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .eq(Article::getId, typeId)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLICITY.name())
        );
        Assert.isFalse(Objects.isNull(article) || article.getTrash().equals(Boolean.TRUE), "文章不存在");
        String userLikeArticleKey = Constants.USER_ARTICLE_LIKE + UserSessionFactory.getUserId();
        if (ObjUtil.isNotNull(article)) {
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(userLikeArticleKey, typeId))) {
                redisTemplate.opsForSet().remove(userLikeArticleKey, typeId);
                redisTemplate.opsForHash().increment(Constants.ARTICLE_LIKE_COUNT, typeId, -1D);
            } else {
                redisTemplate.opsForSet().add(userLikeArticleKey, typeId);
                redisTemplate.opsForHash().increment(Constants.ARTICLE_LIKE_COUNT, typeId, 1D);
            }
        }
    }
}
