package com.zdx.strategy.context;


import com.zdx.enums.ArticleLikeTypeEnum;
import com.zdx.strategy.LikeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LikeStrategyContext {


    private final List<LikeStrategy> likeStrategyList;


    public void executeLikeStrategy(ArticleLikeTypeEnum typeEnum, String typeId) {
        for (LikeStrategy likeStrategy : likeStrategyList) {
            if (likeStrategy.getType().equals(typeEnum)) {
                likeStrategy.like(typeId);
                break;
            }
        }
    }
}
