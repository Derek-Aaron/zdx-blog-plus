package com.zdx.service.us.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.ResetPwd;
import com.zdx.controller.dto.UserStatus;
import com.zdx.entity.us.User;
import com.zdx.mapper.us.UserMapper;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.us.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【us_user】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public Boolean resetPwd(ResetPwd resetPwd) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getPassword, resetPwd.getNewPassword());
        if (ObjUtil.isNotNull(resetPwd.getUserId())) {
            updateWrapper.eq(User::getId, resetPwd.getUserId());
        } else {
            updateWrapper.eq(User::getId, UserSessionFactory.getUserId());
        }
        return update(updateWrapper);
    }

    @Override
    public Boolean updateUserStatus(UserStatus userStatus) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        if (userStatus.getType().equals("disabled")) {
            updateWrapper.set(User::getIsDisabled, userStatus.getIsDisabled());
        }
        if (userStatus.getType().equals("locked")) {
            updateWrapper.set(User::getIsLocked, userStatus.getIsLocked());
        }
        updateWrapper.eq(User::getId, userStatus.getUserId());
        return update(updateWrapper);
    }
}




