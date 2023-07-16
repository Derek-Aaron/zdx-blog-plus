package com.zdx.service.us.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.controller.dto.ResetPwd;
import com.zdx.controller.dto.UserStatus;
import com.zdx.controller.vo.HomeUserInfo;
import com.zdx.entity.us.User;
import com.zdx.mapper.us.UserMapper;
import com.zdx.security.UserSessionFactory;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.us.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author zhaodengxuan
* @description 针对表【us_user】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final RedisTemplate<String, Object> redisTemplate;

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

    @Override
    public List<Map<String, Object>> listUserAll(String words) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(words)) {
            queryWrapper.like(User::getUsername, words);
            queryWrapper.or();
            queryWrapper.like(User::getNickname, words);
            queryWrapper.or();
            queryWrapper.like(User::getEmail, words);
            queryWrapper.or();
            queryWrapper.eq(User::getMobile, words);
        }
        queryWrapper.select(User::getId, User::getUsername);
        queryWrapper.last("limit 10");
        return listMaps(queryWrapper);
    }

    @Override
    public HomeUserInfo  getHomeInfo() {
        UserSession userSession = UserSessionFactory.userDetails();
        HomeUserInfo homeUserInfo = BeanUtil.copyProperties(((UserPrincipal) userSession).getUser(), HomeUserInfo.class);
        Set<Object> articleLikeSet = redisTemplate.opsForSet().members(Constants.USER_ARTICLE_LIKE + userSession.getUserId());
        Set<Object> commentLikeSet = redisTemplate.opsForSet().members(Constants.USER_COMMENT_LIKE + userSession.getUserId());
        Set<Object> talkLikeSet = redisTemplate.opsForSet().members(Constants.USER_TALK_LIKE + userSession.getUserId());
        homeUserInfo.setArticleLikeSet(articleLikeSet);
        homeUserInfo.setCommentLikeSet(commentLikeSet);
        homeUserInfo.setTalkLikeSet(talkLikeSet);
        return homeUserInfo;
    }
}




