package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.Constants;
import com.zdx.entity.zdx.Article;
import com.zdx.enums.ArticleStatusEnum;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.model.vo.front.BlogInfoVO;
import com.zdx.model.vo.front.SiteConfig;
import com.zdx.model.vo.front.UserInfoVo;
import com.zdx.security.UserSessionFactory;
import com.zdx.security.vo.UserPrincipal;
import com.zdx.security.vo.UserSession;
import com.zdx.service.tk.ConfigService;
import com.zdx.service.tk.RedisService;
import com.zdx.service.zdx.BlogService;
import com.zdx.utils.IpAddressUtil;
import com.zdx.utils.ServletUtils;
import com.zdx.utils.UserAgentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final RedisService redisService;

    private final ArticleMapper articleMapper;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final ConfigService configService;
    @Override
    public void result() {
        HttpServletRequest request = ServletUtils.getRequest();
        String ip = IpAddressUtil.getIp(request);
        Map<String, String> map = UserAgentUtils.parseOsAndBrowser(request);
        String os = map.get("os");
        String browser = map.get("browser");
        String md5 = DigestUtils.md5DigestAsHex((ip + os + browser).getBytes(StandardCharsets.UTF_8));
        if (!redisService.hasSetValue(Constants.UNIQUE_VISITOR, md5)) {
            redisService.incr(Constants.BLOG_VIEW_COUNT, 1L);
            redisService.setSet(Constants.UNIQUE_VISITOR, md5);
        }
    }

    @Override
    public BlogInfoVO getBlogInfo() {
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getTrash, Boolean.FALSE).eq(Article::getStatus, ArticleStatusEnum.PUBLICITY.name()));
        Long cateGoryCount = categoryMapper.selectCount(null);
        Long tagCount = tagMapper.selectCount(null);
        Integer count = redisService.getObject(Constants.BLOG_VIEW_COUNT);
        String viewCount = Optional.ofNullable(count).orElse(0).toString();
        String json = configService.getConfig(Constants.BLOG_SITE_CONFIG, String.class);
        SiteConfig siteConfig = JSON.parseObject(json, SiteConfig.class);
        BlogInfoVO blogInfoVO = new BlogInfoVO();
        blogInfoVO.setArticleCount(articleCount);
        blogInfoVO.setCategoryCount(cateGoryCount);
        blogInfoVO.setTagCount(tagCount);
        blogInfoVO.setViewCount(viewCount);
        blogInfoVO.setSiteConfig(siteConfig);
        return blogInfoVO;
    }

    @Override
    public boolean saveSiteConfig(SiteConfig siteConfig) {
        String json = JSON.toJSONString(siteConfig);
        return configService.setConfig(Constants.BLOG_SITE_CONFIG, json);
    }

    @Override
    public SiteConfig getSite() {
        String json = configService.getConfig(Constants.BLOG_SITE_CONFIG, String.class);
        return JSON.parseObject(json, SiteConfig.class);
    }

    @Override
    public UserInfoVo getUserInfo() {
        UserSession userSession = UserSessionFactory.userDetails();
        Long userId = userSession.getUserId();
        UserInfoVo userInfoVo = BeanUtil.copyProperties(((UserPrincipal) userSession).getUser(), UserInfoVo.class);
        Set<Object> articleLikeSet = redisService.getSet(Constants.USER_ARTICLE_LIKE + userId);
        Set<Object> commentLikeSet = redisService.getSet(Constants.USER_COMMENT_LIKE + userId);
        Set<Object> talkLikeSet = redisService.getSet(Constants.USER_TALK_LIKE + userId);
        userInfoVo.setArticleLikeSet(articleLikeSet);
        userInfoVo.setCommentLikeSet(commentLikeSet);
        userInfoVo.setTalkLikeSet(talkLikeSet);
        return userInfoVo;
    }
}
