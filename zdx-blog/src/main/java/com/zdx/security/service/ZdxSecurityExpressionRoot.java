package com.zdx.security.service;

import com.zdx.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;


public class ZdxSecurityExpressionRoot  implements MethodSecurityExpressionOperations {

    protected final Authentication authentication;
    @Setter
    @Getter
    private AuthenticationTrustResolver trustResolver;
    @Setter
    @Getter
    private RoleHierarchy roleHierarchy;
    private Set<String> roles;
    @Setter
    private String defaultRolePrefix = "ROLE_";
    @Setter
    @Getter
    private PermissionEvaluator permissionEvaluator;

    private Object filterObject;
    private Object returnObject;

    private Object target;

    public ZdxSecurityExpressionRoot(Authentication authentication) {
       this.authentication = authentication;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    public void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return target;
    }

    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    public final boolean hasAuthority(String authority) {
        return this.hasAnyAuthority(authority);
    }

    public final boolean hasAnyAuthority(String... authorities) {
        return this.hasAnyAuthorityName(null, authorities);
    }

    public final boolean hasRole(String role) {
        return this.hasAnyRole(role);
    }

    public final boolean hasAnyRole(String... roles) {
        return this.hasAnyAuthorityName(this.defaultRolePrefix, roles);
    }

    private boolean hasAnyAuthorityName(String prefix, String... roles) {
        Set<String> roleSet = this.getAuthoritySet();
        if (roleSet.contains(Constants.ADMIN_AUTH)) {
            return true;
        }
        for (String role : roles) {
            String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
            if (roleSet.contains(defaultedRole)) {
                return true;
            }
        }

        return false;
    }

    public final boolean permitAll() {
        return true;
    }

    public final boolean denyAll() {
        return false;
    }

    public final boolean isAnonymous() {
        return this.trustResolver.isAnonymous(this.authentication);
    }

    public final boolean isAuthenticated() {
        return !this.isAnonymous();
    }

    public final boolean isRememberMe() {
        return this.trustResolver.isRememberMe(this.authentication);
    }

    public final boolean isFullyAuthenticated() {
        return !this.trustResolver.isAnonymous(this.authentication) && !this.trustResolver.isRememberMe(this.authentication);
    }

    public Object getPrincipal() {
        return this.authentication.getPrincipal();
    }


    private Set<String> getAuthoritySet() {
        if (this.roles == null) {
            Collection<? extends GrantedAuthority> userAuthorities = this.authentication.getAuthorities();
            if (this.roleHierarchy != null) {
                userAuthorities = this.roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
            }

            this.roles = AuthorityUtils.authorityListToSet(userAuthorities);
        }

        return this.roles;
    }

    public boolean hasPermission(Object target, Object permission) {
        return this.permissionEvaluator.hasPermission(this.authentication, target, permission);
    }

    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return this.permissionEvaluator.hasPermission(this.authentication, (Serializable)targetId, targetType, permission);
    }
    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role) {
        if (role == null) {
            return null;
        } else if (defaultRolePrefix != null && !defaultRolePrefix.isEmpty()) {
            return role.startsWith(defaultRolePrefix) ? role : defaultRolePrefix + role;
        } else {
            return role;
        }
    }
}
