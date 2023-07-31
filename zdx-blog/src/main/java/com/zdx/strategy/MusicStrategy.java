package com.zdx.strategy;

import com.zdx.enums.MusicTypeEnum;
import com.zdx.model.vo.front.MusicVo;

import java.util.List;

public interface MusicStrategy {


    MusicTypeEnum musicType();

    List<MusicVo> execute(String musicId);
}
