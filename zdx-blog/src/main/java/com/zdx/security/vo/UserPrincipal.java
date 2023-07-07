package com.zdx.security.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserPrincipal implements UserSession, UserDetails, Serializable {

    private User user;

    private List<Role> roles;

    private List<String> permissions;

    private UserAgent userAgent;

    public UserPrincipal() {
    }

    public UserPrincipal(User user, List<Role> roles, List<String> permissions) {
        this.user = user;
        this.roles = roles;
        this.permissions = permissions;
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
        return authorities;
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public Long getUserId() {
        return user.getId();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !user.getIsLocked();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public boolean isEnabled() {
        return !user.getIsDisabled();
    }

    @Override
    @JsonIgnore
    @JSONField(serialize = false)
    public String getNickname() {
        return user.getNickname();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getAvatar() {
        return user.getAvatar();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getMobile() {
        return user.getMobile();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getPersonId() {
        return user.getPersonId();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public String getGender() {
        return user.getGender();
    }

    @Override
    @JSONField(serialize = false)
    @JsonIgnore
    public List<String> getRoleIds() {
        return roles.stream().map(role -> String.valueOf(role.getId())).toList();
    }

    @Override
    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public List<String> getPermissions() {
        return permissions;
    }
}
