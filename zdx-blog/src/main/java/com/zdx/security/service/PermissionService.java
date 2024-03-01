package com.zdx.security.service;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdx.Constants;
import com.zdx.entity.us.Acl;
import com.zdx.entity.us.Role;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.tk.RedisService;
import com.zdx.service.us.AclService;
import com.zdx.service.us.RoleService;
import com.zdx.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class PermissionService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AclService aclService;


    /**
     * 获取全部的权限
     * @param userId 用户id
     * @return 权限列表
     */

    public List<String> getPermissions(Long userId) {
        List<Role> roles = roleService.getRolesByUserId(userId);
        List<String> roleIds = new ArrayList<>(roles.stream().map(role -> Constants.ACL_ROLE_PREFIX + role.getId()).toList());
        List<String> ids = new ArrayList<>(roleIds);
        ids.add(Constants.ACL_USER_PREFIX + userId);
        List<Acl> acls = aclService.getAclsBySubject(ids);
        List<String> aclList = acls.stream().map(acl -> String.valueOf(acl.getParams())).toList();
        if (ObjUtil.isNotNull(aclList) && !aclList.isEmpty()) {
            aclList = new ArrayList<>(aclList);
        } else {
            aclList = new ArrayList<>();
        }
        if (roleIds.contains(Constants.ACL_ROLE_PREFIX + Role.ADMIN_ROLE_ID)) {
            aclList.add(Constants.ADMIN_AUTH);
        }
        aclList.addAll(roles.stream().map(role -> "ROLE_" + role.getName()).toList());
        return aclList;
    }

    /**
     * 通过请求过去用户信息
     * @param request 请求
     * @return 用户信息
     */
    public UserSession getLoginUserInfo(HttpServletRequest request) {
        String token = getToken(request);
        if (StrUtil.isNotBlank(token)) {
            try {
                String uuid = JWTUtil.parseToken(token);
                String userKey = getTokenKey(uuid);
                Object user = redisService.getObject(userKey);
                if (user instanceof UserPrincipal userSession) {
                    return userSession;
                }
                if (user instanceof JSONObject json) {
                    return JSON.parseObject(json.toString(), UserPrincipal.class);
                }
                if (user instanceof String str) {
                    return JSON.parseObject(str, UserPrincipal.class);
                }
                if (user instanceof Map<?,?> map) {
                    return BeanUtil.fillBeanWithMap(map, new UserPrincipal(), false);
                }
            } catch (Exception e) {
                log.error("解析json异常：{}", e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    /**
     * 创建token
     * @param userSession 用户信息
     * @return token
     */
    public String createToken(UserSession userSession) {
        refreshToken(userSession);
        return JWTUtil.generateToken(userSession.getPersonId());
    }

    /**
     * 刷新用户权限
     * @param userSession 用户信息
     */
    public void refreshToken(UserSession userSession) {
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(userSession.getPersonId());
        Object val = redisService.getObject(userKey);
        if (ObjUtil.isNotNull(val)) {
            redisService.setExpire(userKey, Constants.EXPIRETIME, TimeUnit.MINUTES);
        } else {
            redisService.setObject(userKey, userSession, Constants.EXPIRETIME, TimeUnit.MINUTES);
        }
    }

    protected String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(Constants.AUTHORIZATION);
        if (StrUtil.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 注销登录
     * @param personId 用户 personId
     */
    @CacheEvict(cacheNames = Constants.ROUTER_KEY, key = "#personId")
    public void logout(String personId) {
        redisService.deleteObject(Constants.LOGIN_TOKEN_KEY + personId);
    }

}
