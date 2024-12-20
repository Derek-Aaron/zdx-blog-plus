package com.zdx;

import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpUtil;
import com.zdx.entity.tk.Dict;
import com.zdx.entity.tk.Menu;
import com.zdx.entity.us.Role;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Navigation;
import com.zdx.enums.*;
import com.zdx.event.EventObject;
import com.zdx.model.dto.MailDto;
import com.zdx.model.vo.front.MusicVo;
import com.zdx.security.service.LoginService;
import com.zdx.service.tk.DictService;
import com.zdx.service.tk.MenuService;
import com.zdx.service.us.RoleService;
import com.zdx.service.us.UserService;
import com.zdx.service.zdx.NavigationService;
import com.zdx.strategy.context.StrategyContext;
import com.zdx.utils.RsaUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        role.setName("demonstration");
        role.setDisplay("演示角色");
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

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void testEs() {
        MailDto mailDto = MailDto.builder().build();
        mailDto.setSubject("测试");
        mailDto.setToEmail("2488288090@qq.com");
        mailDto.setContent("测试");
        EventObject event = new EventObject(mailDto, EventObject.Attribute.SEND_EMAIL_KEY);
        event.setAttribute(EventObject.Attribute.SEND_EMAIL_TYPE, SendEmailEnum.SIMPLE);
        applicationContext.publishEvent(event);
    }

    @Autowired
    private LoginService  loginService;
    @Test
    public void sendEmail() {
       loginService.sendCode("2488288090@qq.com");
    }

    @Autowired
    private StrategyContext strategyContext;
    @Test
    public void testMusic() {
        List<MusicVo> musicVos = strategyContext.executeMusic(MusicTypeEnum.QQ, "");
        System.out.println(musicVos);
    }

    @Test
    public void test11() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String str = restTemplate.getForObject("https://www.emojiall.com/zh-hans/image-emoji-platform/skype", String.class);
        assert str != null;
        Document document = Jsoup.parse(str);
        Elements elements = document.getElementsByTag("img");
        for (Element element : elements) {
            if (element.hasAttr("data-src")) {
                String src = element.attr("data-src");
                String title = element.attr("title");
                byte[] bytes = HttpUtil.downloadBytes("https://www.emojiall.com" + src);
                FileOutputStream fos = new FileOutputStream("/Users/zhaodengxuan/Downloads/" + title + ".png");
                fos.write(bytes);
                IoUtil.writeUtf8(fos, true);
            }
        }
    }

    @Autowired
    private NavigationService navigationService;
    @Test
    public void test111() throws IOException {

        Map<String, List<Navigation>> map = navigationService.list().stream().collect(Collectors.groupingBy(Navigation::getGroup, Collectors.toList()));
        for (Map.Entry<String, List<Navigation>> entry : map.entrySet()) {
            NavigationGroupEnum groupEnum = NavigationGroupEnum.valueOf(entry.getKey());
            List<Navigation> list = entry.getValue().stream().peek(item -> item.setOrder(groupEnum.ordinal())).toList();
            navigationService.saveOrUpdateBatch(list);
        }
    }

}
