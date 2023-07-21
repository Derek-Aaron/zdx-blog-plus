package com.zdx.strategy.context;


import com.zdx.enums.AuthSourceEnum;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.strategy.AuthStrategy;
import com.zdx.strategy.LikeStrategy;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StrategyContext {

    private final List<LikeStrategy> likeStrategies;

    private final List<AuthStrategy> authStrategies;


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

    public AuthRequest executeAuth(AuthSourceEnum sourceEnum, String source, String callbackUrl) {
        for (AuthStrategy authStrategy : authStrategies) {
            if (authStrategy.source().equals(sourceEnum)) {
                return authStrategy.execute(source, callbackUrl);
            }
        }
        return null;
    }
}
