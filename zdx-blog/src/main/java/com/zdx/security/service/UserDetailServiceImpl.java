package com.zdx.security.service;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.us.Account;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.service.us.AccountService;
import com.zdx.service.us.RoleService;
import com.zdx.service.us.UserService;
import com.zdx.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Account> accountQueryWrapper = new LambdaQueryWrapper<>();
        accountQueryWrapper.eq(Account::getUsername, username);
        Account account = accountService.getOne(accountQueryWrapper);
        User user = null;
        if (ObjUtil.isNull(account)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username);
            user = userService.getOne(queryWrapper);
        } else {
            user = userService.getById(account.getUserId());
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
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> aclList = permissionService.getPermissions(user.getId());
        return new UserPrincipal(user, roles, aclList);
    }
}
