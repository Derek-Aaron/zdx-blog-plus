package com.zdx.security.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.Constants;
import com.zdx.entity.tk.Menu;
import com.zdx.entity.us.Acl;
import com.zdx.entity.us.Auth;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.enums.MenuTypeEnum;
import com.zdx.event.EventObject;
import com.zdx.model.dto.AclDto;
import com.zdx.model.dto.MailDto;
import com.zdx.model.dto.UserLogin;
import com.zdx.model.vo.Router;
import com.zdx.security.UserSessionFactory;
import com.zdx.security.vo.UserAgent;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.service.tk.EmailService;
import com.zdx.service.tk.MenuService;
import com.zdx.service.tk.RedisService;
import com.zdx.service.us.AclService;
import com.zdx.service.us.AuthService;
import com.zdx.service.us.RoleService;
import com.zdx.service.us.UserService;
import com.zdx.utils.IpAddressUtil;
import com.zdx.utils.UserAgentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
public class LoginService {


    private final AuthenticationManager authenticationManager;


    private final PermissionService permissionService;


    private final ApplicationContext applicationContext;

    private final RedisService redisService;


    private final MenuService menuService;

    private final RoleService roleService;


    private final AclService aclService;


    private final AuthService authService;


    private final UserService userService;


    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    /**
     * 登录
     *
     * @param request   请求
     * @param userLogin 请求参数
     * @return token
     */
    public String login(HttpServletRequest request, UserLogin userLogin) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
        UserPrincipal userPrincipal = (UserPrincipal) Objects.requireNonNull(authenticate).getPrincipal();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // 生成token
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        loginEvent(request, userPrincipal);
        return permissionService.createToken(userPrincipal);
    }


    /**
     * 返回当前登录用户的系统信息
     *
     * @param request       请求
     * @param userPrincipal 用户实体
     * @return 系统信息
     */
    private UserAgent getUserAgent(HttpServletRequest request, UserPrincipal userPrincipal) {
        UserAgent userAgent = new UserAgent();
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request);
        userAgent.setOs(userAgentMap.get("os"));
        userAgent.setPersonId(userPrincipal.getPersonId());
        userAgent.setBrowser(userAgentMap.get("browser"));
        userAgent.setLoginTime(new Date());
        userAgent.setNickname(userPrincipal.getNickname());
        userAgent.setUsername(userPrincipal.getUsername());
        userAgent.setLoginTime(new Date());
        String ip = IpAddressUtil.getIp(request);
        userAgent.setIp(ip);
        userAgent.setAddress(IpAddressUtil.getCityInfo(ip));
        return userAgent;
    }

    /**
     * 发起登录成功事件
     *
     * @param request       请求
     * @param userPrincipal 用户实体
     */
    private void loginEvent(HttpServletRequest request, UserPrincipal userPrincipal) {
        log.info("用户【{}】,ip【{}】,登录成功", userPrincipal.getUser().getUsername(), IpAddressUtil.getIp(request));
        EventObject event = new EventObject(userPrincipal, EventObject.Attribute.LOGINLOG);
        event.setAttribute(EventObject.Attribute.REQUEST, request);
        applicationContext.publishEvent(event);
        UserAgent userAgent = getUserAgent(request, userPrincipal);
        userPrincipal.setUserAgent(userAgent);
    }


    @Cacheable(cacheNames = Constants.ROUTER_KEY, key = "T(com.zdx.security.UserSessionFactory).personId")
    public List<Router> routers() {
        List<Menu> menus = new ArrayList<>();
        if (UserSessionFactory.hasRole(Role.ADMIN_ROLE_ID)) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getType, Arrays.asList(MenuTypeEnum.MENU.name(), MenuTypeEnum.DIRECTORY.name()));
            queryWrapper.eq(Menu::getIsDisabled, Boolean.FALSE);
            menus = menuService.list(queryWrapper);
        } else {
            List<String> roleIds = UserSessionFactory.getRoleIds().stream().map(r -> Constants.ACL_ROLE_PREFIX + r).toList();
            List<String> subjectIds = new ArrayList<>(roleIds);
            subjectIds.add(Constants.ACL_USER_PREFIX + UserSessionFactory.getUserId());
            List<Acl> acls = aclService.getAclsBySubject(subjectIds);
            List<String> menuIds = acls.stream().map(acl -> acl.getResource().replaceAll(Constants.ACL_MENU_PREFIX, "")).toList();
            if (!menuIds.isEmpty()) {
                LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.in(Menu::getId, menuIds);
                queryWrapper.in(Menu::getType, Arrays.asList(MenuTypeEnum.MENU.name(), MenuTypeEnum.DIRECTORY.name()));
                queryWrapper.eq(Menu::getIsDisabled, Boolean.FALSE);
                menus = menuService.list(queryWrapper);
            }
        }
        return menus.stream().map(menu -> BeanUtil.copyProperties(menu, Router.class)).toList();
    }

    public String authLogin(AuthUser authUser, String type, HttpServletRequest request) {
        Auth auth = authService.getAuthBySourceAndType(authUser.getSource(), type);
        User user = null;
        if (ObjUtil.isNull(auth.getUserId())) {
            user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, authUser.getUsername()));
            if (ObjUtil.isNull(user)) {
                user = new User();
            }
            user.setUsername(authUser.getUsername());
            user.setEmail(authUser.getEmail());
            user.setPassword(passwordEncoder.encode(RandomUtil.randomString(8)));
            user.setNickname(authUser.getNickname());
            user.setAvatar(authUser.getAvatar());
            userService.saveOrUpdate(user);
            addRoleByUser(user);
            auth.setUsername(user.getUsername());
            auth.setSource(authUser.getSource());
            auth.setUserId(user.getId());
            authService.saveOrUpdate(auth);
        } else {
            user = userService.getById(auth.getUserId());
            addRoleByUser(user);
        }
        List<Role> roles = roleService.getRolesByUserId(auth.getUserId());
        List<String> aclList = permissionService.getPermissions(auth.getUserId());
        UserPrincipal userPrincipal = new UserPrincipal(user, roles, aclList);
        loginEvent(request, userPrincipal);
        return permissionService.createToken(userPrincipal);
    }

    private void addRoleByUser(User user) {
        List<Role> roles = roleService.getRolesByUserId(user.getId());
        List<String> roleIds = roles.stream().map(role -> String.valueOf(role.getId())).toList();
        if (!roleIds.contains(Role.BLOG_USE_ID)) {
            AclDto aclVo = new AclDto();
            aclVo.setType("add");
            aclVo.setSubjects(Collections.singletonList(String.valueOf(user.getId())));
            aclVo.setResources(List.of(Role.BLOG_USE_ID));
            roleService.addOrDelResources(aclVo);
        }
    }

    public void sendCode(String email) {
        String code = RandomUtil.randomNumbers(6);
        MailDto mailDto = MailDto.builder()
                .toEmail(email)
                .subject("验证吗")
                .content(StrUtil.format("您的验证码为：{} 有效期为：{}分钟", code, Constants.CODE_EXPIRE_TIME))
                .build();
        emailService.sendSimple(mailDto);
        redisService.setObject(Constants.CODE_KEY + email, code, Constants.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }
}
