package com.zdx.service.blog;

import com.zdx.controller.vo.BlogInfoVO;

public interface BlogInfoService {

    /**
     * 获取博客网站配置信息
     * @return 返回
     */
    BlogInfoVO getBlogInfo();

    /**
     * 上传访客信息
     */
    void report();

}
