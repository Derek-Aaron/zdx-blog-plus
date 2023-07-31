package com.zdx.strategy.impl;


import com.zdx.enums.MusicTypeEnum;
import com.zdx.model.vo.front.MusicVo;
import com.zdx.strategy.MusicStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocalityMusicStrategyImpl implements MusicStrategy {
    @Override
    public MusicTypeEnum musicType() {
        return MusicTypeEnum.LOCALITY;
    }

    @Override
    public List<MusicVo> execute(String musicId) {
        return null;
    }
}
