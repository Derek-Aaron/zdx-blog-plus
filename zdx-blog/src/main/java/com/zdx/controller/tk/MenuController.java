package com.zdx.controller.tk;


import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.Constants;
import com.zdx.annotation.Log;
import com.zdx.controller.BaseController;
import com.zdx.model.dto.MenuStatic;
import com.zdx.model.dto.RequestParams;
import com.zdx.entity.tk.Menu;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.service.tk.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zdx.menu")
@Api(tags = "菜单管理")
@Validated
@RequiredArgsConstructor
public class MenuController extends BaseController<Menu> {

    private final MenuService menuService;

    @Override
    public IService<Menu> getService() {
        return menuService;
    }

    @Override
    public Wrapper<Menu> getQueryWrapper(RequestParams params) {
        return null;
    }

    @GetMapping("/tree")
    @ApiOperation("获取树型数据")
    public Result<List<Tree<Menu>>> tree(RequestParams params) {
        return Result.success(menuService.tree(params));
    }


    @PostMapping("/updateMenuStatic")
    @Log(type = LogEventEnum.SAVE, desc = "更改菜单状态")
    @ApiOperation("更改菜单状态")
    public Result<String> updateMenuStatic(@RequestBody @Validated MenuStatic menuStatic) {
        return menuService.updateMenuStatic(menuStatic) ? Result.success() : Result.error();
    }


    @Override
    @PostMapping("/save")
    @Log(type = LogEventEnum.SAVE, desc = "保存菜单")
    @ApiOperation("保存菜单")
    @CacheEvict(cacheNames = Constants.ROUTER_KEY, key = "T(com.zdx.security.UserSessionFactory).personId")
    public Result<String> save(@RequestBody Menu data) {
        return super.save(data);
    }
}
