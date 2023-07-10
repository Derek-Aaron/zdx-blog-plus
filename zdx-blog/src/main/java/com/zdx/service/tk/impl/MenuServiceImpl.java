package com.zdx.service.tk.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.MenuStatic;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.tk.Menu;
import com.zdx.mapper.tk.MenuMapper;
import com.zdx.service.tk.MenuService;
import com.zdx.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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




