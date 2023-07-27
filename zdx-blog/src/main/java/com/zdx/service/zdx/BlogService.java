package com.zdx.service.zdx;

import com.zdx.model.vo.BlogBackInfoVO;
import com.zdx.model.vo.front.BlogInfoVO;
import com.zdx.model.vo.front.SiteConfig;
import com.zdx.model.vo.front.UserInfoVo;

public interface BlogService {
    /**
     * 上传用户访问信息
     */
    void result();

    /**
     * 查询博客信息
     * @return
     */
    BlogInfoVO getBlogInfo();

    /**
     * 保存博客配置信息
     * @param siteConfig 博客配置信息
     * @return 返回
     */
    boolean saveSiteConfig(SiteConfig siteConfig);

    /**
     * 获取网站配置
     * @return 返回
     */
    SiteConfig getSite();

    /**
     * 获取当前登录用户信息
     * @return 返回
     */
    UserInfoVo getUserInfo();

    /**
     * 后台首页获取博客信息
     * @return 返回
     */
    BlogBackInfoVO adminBlogInfo();
}
