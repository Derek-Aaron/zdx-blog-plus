package com.zdx.strategy;

import com.zdx.enums.ArticleLikeTypeEnum;

public interface LikeStrategy {

    ArticleLikeTypeEnum getType();


    void like(String typeId);
}
