package com.zdx;


import com.zdx.wx.service.WxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WxTest {

    @Autowired
    private WxService wxService;


}
