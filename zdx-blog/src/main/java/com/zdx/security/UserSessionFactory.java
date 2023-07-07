package com.zdx.security;

import cn.hutool.core.util.ObjUtil;
import com.zdx.entity.us.Role;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.utils.MessageUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

public class UserSessionFactory{

    /**
     * 获取当前登录用户
     * @return
     */
    public static Optional<UserSession> currentUser()  {
        Object principal = getAuthentication().getPrincipal();
        if (principal instanceof UserPrincipal userDetails) {
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }

    public static UserSession userDetails() {
        return  currentUser().orElseThrow(() -> new AuthenticationServiceException(MessageUtil.message("zdx.authentication.error")));
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    public static Long getUserId() {
        return userDetails().getUserId();
    }
    public static String getUsername() {
        return userDetails().getUsername();
    }

    public static String getNickname() {
        return userDetails().getNickname();

    }

    public static String getAvatar() {
        return userDetails().getAvatar();
    }

    public static String getEmail() {
        return userDetails().getEmail();
    }

    public static String getMobile() {
        return userDetails().getMobile();
    }

    public static String getPersonId() {
        return userDetails().getPersonId();
    }

    public static String getGender() {
        return userDetails().getGender();
    }

    public static List<String> getRoleIds() {
        return  userDetails().getRoleIds();
    }

    public static List<Role> getRoles() {
        return userDetails().getRoles();
    }

    public static List<String> getPermissions() {
        return  userDetails().getPermissions();
    }

    public static   Boolean hasRole(String roleId) {
        return getRoles().stream().map(role -> String.valueOf(role.getId())).toList().contains(roleId);
    }

    public static Boolean hasRoles(String... roleIds) {
        if (ObjUtil.isNull(roleIds)) {
            return false;
        }
        for (String roleId : roleIds) {
            return hasRole(roleId);
        }
        return false;
    }

    public static Boolean hasPermission(String aclId) {
        return getPermissions().contains(aclId);
    }

    public static Boolean hasPermissions(String... aclIds) {
        if (ObjUtil.isNull(aclIds)) {
            return false;
        }
        for (String aclId : aclIds) {
            return hasPermission(aclId);
        }
        return false;
    }
}
