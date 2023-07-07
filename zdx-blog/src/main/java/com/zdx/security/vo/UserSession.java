package com.zdx.security.vo;

import cn.hutool.core.util.ObjUtil;
import com.zdx.entity.us.Role;

import java.util.List;

public interface UserSession {

    Long getUserId();

    String getUsername();

    String getNickname();

    String getAvatar();

    String getEmail();

    String getMobile();

    String getPersonId();

    String getGender();

    List<String>  getRoleIds();

    List<Role> getRoles();

    List<String> getPermissions();

    default  Boolean hasRole(String roleId) {
        return getRoles().stream().map(role -> String.valueOf(role.getId())).toList().contains(roleId);
    }

    default Boolean hasRoles(String... roleIds) {
        if (ObjUtil.isNull(roleIds)) {
            return false;
        }
        for (String roleId : roleIds) {
            return hasRole(roleId);
        }
        return false;
    }

    default Boolean hasPermission(String aclId) {
        return getPermissions().contains(aclId);
    }

    default Boolean hasPermissions(String... aclIds) {
        if (ObjUtil.isNull(aclIds)) {
            return false;
        }
        for (String aclId : aclIds) {
            return hasPermission(aclId);
        }
        return false;
    }


}
