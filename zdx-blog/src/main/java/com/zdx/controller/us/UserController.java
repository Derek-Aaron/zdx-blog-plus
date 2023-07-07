package com.zdx.controller.us;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.controller.dto.ResetPwd;
import com.zdx.controller.vo.UserProfile;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.event.EventObject;
import com.zdx.handle.Result;
import com.zdx.security.UserSessionFactory;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.us.UserService;
import com.zdx.utils.MessageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/zdx.user")
@RequiredArgsConstructor
@Validated
@Api("用户管理")
public class UserController {

    private final UserService userService;

    private final ApplicationContext applicationContext;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/profile")
    @ApiOperation("查询个人用户信息")
    public Result<UserProfile> profile() {
        UserSession userSession = UserSessionFactory.userDetails();
        UserProfile profile = BeanUtil.copyProperties(((UserPrincipal) userSession).getUser(), UserProfile.class);
        String roleNames = UserSessionFactory.getRoles().stream().map(Role::getDisplay).collect(Collectors.joining(","));
        profile.setRoleNames(roleNames);
        return Result.success(profile);
    }

    @PostMapping("/updateProfile")
    @ApiOperation("更改个人用户信息")
    public Result<String> updateProfile(@RequestBody @Validated UserProfile userProfile) {
        User user = BeanUtil.copyProperties(userProfile, User.class);
        user.setId(UserSessionFactory.getUserId());
        if (userService.saveOrUpdate(user)) {
            EventObject event = new EventObject(userProfile, EventObject.Attribute.REFRESH_LOGIN_TOKEN_CACHE);
            event.setAttribute("userSession", UserSessionFactory.userDetails());
            applicationContext.publishEvent(event);
            return Result.success();
        }
        return Result.error();
    }

    @PostMapping("/resetPwd")
    @ApiOperation("重置密码")
    public Result<String> resetPwd(@RequestBody @Validated ResetPwd resetPwd) {
        if (StrUtil.isNotBlank(resetPwd.getOldPassword())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(User::getPassword);
            queryWrapper.eq(User::getId, UserSessionFactory.getUserId());
            User user = userService.getOne(queryWrapper);
            if (ObjUtil.isNotNull(user)) {
                if (!passwordEncoder.matches(resetPwd.getNewPassword(), user.getPassword())) {
                    return Result.error(MessageUtil.getLocaleMessage("zdx.password.verify"));
                }
            }
        }
        resetPwd.setNewPassword(passwordEncoder.encode(resetPwd.getNewPassword()));
        if (userService.resetPwd(resetPwd)) {
            return Result.success();
        }
        return Result.error();
    }
}
