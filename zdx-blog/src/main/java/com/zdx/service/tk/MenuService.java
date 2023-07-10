package com.zdx.service.tk;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.dto.MenuStatic;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.tk.Menu;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【tk_menu】的数据库操作Service
* @createDate 2023-07-05 17:32:37
*/
public interface MenuService extends IService<Menu> {

    List<Tree<Menu>> tree(RequestParams params);

    Boolean updateMenuStatic(MenuStatic menuStatic);
}
