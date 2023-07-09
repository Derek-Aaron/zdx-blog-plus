package com.zdx.service.tk.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.controller.dto.MenuStatic;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.tk.Menu;
import com.zdx.entity.us.Acl;
import com.zdx.entity.us.Role;
import com.zdx.controller.vo.Router;
import com.zdx.enums.MenuTypeEnum;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.tk.MenuService;
import com.zdx.mapper.tk.MenuMapper;
import com.zdx.service.us.AclService;
import com.zdx.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【tk_menu】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

   private final AclService aclService;

    @Override
    public List<Router> routers() {
        List<Menu> menus = new ArrayList<>();
        if (UserSessionFactory.hasRole(Role.ADMIN_ROLE_ID)) {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getType, Arrays.asList(MenuTypeEnum.MENU.name(), MenuTypeEnum.DIRECTORY.name()));
            queryWrapper.eq(Menu::getIsDisabled, Boolean.FALSE);
            menus = list(queryWrapper);
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
                menus = list(queryWrapper);
            }

        }
        return menus.stream().map(menu -> BeanUtil.copyProperties(menu, Router.class)).toList();
    }

    @Override
    public List<Tree<Menu>> tree(RequestParams params) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Menu::getName, params.getParam("name"));
        queryWrapper.eq(params.hasParam("isDisabled"), Menu::getIsDisabled, params.getParam("isDisabled", Boolean.class));
        return TreeUtil.createTree(list(queryWrapper), "order");
    }

    @Override
    public Boolean updateMenuStatic(MenuStatic menuStatic) {
        LambdaUpdateWrapper<Menu> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Menu::getIsDisabled, menuStatic.getIsDisabled());
        updateWrapper.eq(Menu::getId, menuStatic.getMenuId());
        return update(updateWrapper);
    }

}




