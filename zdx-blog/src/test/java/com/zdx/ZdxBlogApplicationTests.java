package com.zdx;

import com.zdx.entity.tk.Dict;
import com.zdx.entity.tk.Menu;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.enums.DictTypeEnum;
import com.zdx.enums.GenderEnum;
import com.zdx.enums.MenuTypeEnum;
import com.zdx.service.tk.DictService;
import com.zdx.service.tk.MenuService;
import com.zdx.service.us.RoleService;
import com.zdx.service.us.UserService;
import com.zdx.utils.RsaUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ZdxBlogApplicationTests {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        User user = new User();
        user.setId(1676767825482121217L);
        user.setUsername("admin");
        user.setNickname("网主");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEmail("2488288090@qq.com");
        user.setMobile("17630406972");
        user.setGender(GenderEnum.MALE.name());
        userService.saveOrUpdate(user);
    }

    @Autowired
    private RoleService roleService;

    @Test
    public void addRole() {
        Role role = new Role();
        role.setName("admin");
        role.setDisplay("超级管理员");
        roleService.saveOrUpdate(role);
    }

    @Autowired
    private MenuService menuService;

    @Test
    public void addMenu() {
        Menu menu = new Menu();
        menu.setName("菜单管理");
        menu.setType(MenuTypeEnum.MENU.name());
        menu.setIcon("menu");
        menu.setParentId(1676834060471316481L);
        menu.setParams("zdx:menu");
        menuService.saveOrUpdate(menu);
    }

    @Autowired
    private DictService dictService;
    @Test
    public void addDict() {
        Dict dict = new Dict();
        dict.setName("字典类型");
        dict.setKey("zdx_dict_type");
        dict.setType(DictTypeEnum.ENUMS.name());
        dict.setInvoke("class:com.zdx.enums.DictTypeEnum");
        dictService.saveDict(dict);
    }

    @Test
    public void test1() {
        System.out.println(RsaUtil.decrypt("H4yU/Ci0dqKMzpAwnBQyjJTrPaksWXOFsFm92psAPzNsDemJhiGBQEHcPxNFCKG9Bxx21Do2cX8SzY***Z8pI6YbBDmdqegma4gFbl2T/Ei95DnqEiPX20VB5M5tCCTrPpEJpcZE/2sdnfI/uFu56D9BA6aIcTdCBjlL***TD98EAp8x9GsGBgkeMmq4qvkHEN5tlI5Y2n+WNSjXDjyHHlKb8Ii8ykdk9qHlJ3ZAqqvkq***yjdhO+KnVR5TIK+HGqHfsY9F2HnzdwLNWFCoX3RMm1iUfIxJkw/yIq976rtKboAXE0uIIiMO2KQUX3Yh9OzpXq4H2R/K7iCtkYMPMFXIg=="));
    }

}
