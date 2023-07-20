package com.zdx.strategy;


import com.zdx.enums.LikeTypeEnum;

public interface LikeStrategy {

    LikeTypeEnum likeType();


    void execute(String typeId);
}
