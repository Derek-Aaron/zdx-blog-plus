package com.zdx.strategy.context;


import com.zdx.entity.tk.Dict;
import com.zdx.entity.us.Auth;
import com.zdx.enums.AuthSourceEnum;
import com.zdx.enums.DictTypeEnum;
import com.zdx.enums.LikeTypeEnum;
import com.zdx.strategy.AuthStrategy;
import com.zdx.strategy.DictStrategy;
import com.zdx.strategy.LikeStrategy;
import lombok.RequiredArgsConstructor;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StrategyContext {

    private final List<LikeStrategy> likeStrategies;

    private final List<AuthStrategy> authStrategies;

    private final List<DictStrategy> dictStrategies;


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

    /**
     * 第三方登录
     * @param sourceEnum 登录类型
     * @param auth 实体类
     * @return 返回
     */
    public AuthRequest executeAuth(AuthSourceEnum sourceEnum, Auth auth) {
        for (AuthStrategy authStrategy : authStrategies) {
            if (authStrategy.source().equals(sourceEnum)) {
                return authStrategy.execute(auth);
            }
        }
        return null;
    }

    /**
     * 数据字典映射
     * @param typeEnum 数据字典类型
     * @param dict 数据字典
     * @return 返回
     */
    public List<Map<String, String>> executeDict(DictTypeEnum typeEnum, Dict dict) {
        for (DictStrategy dictStrategy : dictStrategies) {
            if (dictStrategy.type().equals(typeEnum)) {
                return dictStrategy.execute(dict);
            }
        }
        return null;
    }
}
