package com.zdx.security.service;

import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.service.us.AccountService;
import com.zdx.service.us.RoleService;
import com.zdx.service.us.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = userService.getUserByUserName(username);
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> aclList = permissionService.getPermissions(user.getId());
        return new UserPrincipal(user, roles, aclList);
    }
}
