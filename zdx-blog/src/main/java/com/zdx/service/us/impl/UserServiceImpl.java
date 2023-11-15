package com.zdx.service.us.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Account;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.exception.BaseException;
import com.zdx.mapper.us.AccountMapper;
import com.zdx.mapper.us.RoleMapper;
import com.zdx.mapper.us.UserMapper;
import com.zdx.model.dto.RegisterDto;
import com.zdx.model.dto.ResetPwd;
import com.zdx.model.dto.UserStatus;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.us.UserService;
import com.zdx.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
* @author zhaodengxuan
* @description 针对表【us_user】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    private final AccountMapper accountMapper;

    private final RoleMapper roleMapper;


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
    public User getUserByUserName(String username) {
        LambdaQueryWrapper<Account> accountQueryWrapper = new LambdaQueryWrapper<>();
        accountQueryWrapper.eq(Account::getUsername, username);
        Account account = accountMapper.selectOne(accountQueryWrapper);
        User user = null;
        if (ObjUtil.isNull(account)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username);
            user = baseMapper.selectOne(queryWrapper);
        } else {
            user = getById(account.getUserId());
            user.setUsername(username);
            user.setPassword(account.getPassword());
            user.setIsLocked(account.getIsLocked());
            user.setIsDisabled(account.getIsDisabled());
        }
        if (ObjUtil.isNull(user)) {
            throw new BadCredentialsException(MessageUtil.message("zdx.user.null"));
        }
        if (user.getIsLocked()) {
            throw new LockedException(MessageUtil.message("zdx.user.locking"));
        }
        if (user.getIsDisabled()) {
            throw new DisabledException(MessageUtil.message("zdx.user.disable"));
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean register(RegisterDto register) {
        LambdaQueryWrapper<Account> accountQueryWrapper = new LambdaQueryWrapper<>();
        accountQueryWrapper.eq(Account::getUsername, register.getUsername());
        Account account = accountMapper.selectOne(accountQueryWrapper);
        if (ObjUtil.isNotNull(account)) {
            throw new BaseException("zdx.user.register");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, register.getUsername());
        User user = getOne(queryWrapper);
        if (ObjUtil.isNotNull(user)) {
            throw new BaseException("zdx.user.register");
        }
        user = new User();
        user.setUsername(register.getUsername());
        user.setNickname(register.getUsername());
        user.setEmail(register.getEmail());
        user.setPassword(register.getPassword());
        if (saveOrUpdate(user)) {
            roleMapper.addResources(String.valueOf(user.getId()), List.of(Role.BLOG_USE_ID));
            return true;
        }
        return false;
    }
}




