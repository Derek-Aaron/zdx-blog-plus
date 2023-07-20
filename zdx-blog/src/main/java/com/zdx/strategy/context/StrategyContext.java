package com.zdx.strategy.context;


import com.zdx.enums.LikeTypeEnum;
import com.zdx.strategy.LikeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StrategyContext {

    private final List<LikeStrategy> likeStrategies;


    /**
     * 执行点赞策略
     * @param typeEnum 点赞类型
     * @param typeId 点赞id
     */
    public void executeLike(LikeTypeEnum typeEnum, String typeId) {
        for (LikeStrategy likeStrategy : likeStrategies) {
            if (likeStrategy.likeType().equals(typeEnum)) {
                likeStrategy.execute(typeId);
                break;
            }
        }
    }
}
