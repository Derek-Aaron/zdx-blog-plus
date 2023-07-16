package com.zdx.service.blog.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.Constants;
import com.zdx.controller.vo.BlogInfoVO;
import com.zdx.entity.zdx.Article;
import com.zdx.enums.ArticleStatusEnum;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.mapper.zdx.TagMapper;
import com.zdx.service.blog.BlogInfoService;
import com.zdx.utils.IpAddressUtil;
import com.zdx.utils.ServletUtils;
import com.zdx.utils.UserAgentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class BlogInfoServiceImpl implements BlogInfoService {

    private final ArticleMapper articleMapper;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    @Override
    public BlogInfoVO getBlogInfo() {
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>().eq(Article::getStatus, ArticleStatusEnum.PUBLICITY.name()).eq(Article::getTrash, Boolean.FALSE));
        Long categoryCount = categoryMapper.selectCount(null);
        Long tagCount = tagMapper.selectCount(null);
        Object blogCount = redisTemplate.opsForValue().get(Constants.BLOG_VIEW_COUNT);
        String viewCount = Optional.ofNullable(blogCount).orElse(0).toString();
        BlogInfoVO blogInfoVO = new BlogInfoVO();
        blogInfoVO.setArticleCount(articleCount);
        blogInfoVO.setCategoryCount(categoryCount);
        blogInfoVO.setTagCount(tagCount);
        blogInfoVO.setViewCount(viewCount);
        return blogInfoVO;
    }

    @Override
    public void report() {
        HttpServletRequest request = ServletUtils.getRequest();
        String ipAddress = IpAddressUtil.getIp(request);
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request);
        // 获取访问设备
        String browser = userAgentMap.get("browser");
        String os = userAgentMap.get("os");
        // 生成唯一用户标识
        String uuid = ipAddress + browser + os;
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        if (!Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(Constants.UNIQUE_VISITOR, md5))) {
            // 访问量+1
            redisTemplate.opsForValue().increment(Constants.BLOG_VIEW_COUNT, 1);
            // 保存唯一标识
            redisTemplate.opsForSet().add(Constants.UNIQUE_VISITOR, md5);
        }
    }
}
