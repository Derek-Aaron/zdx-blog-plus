package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Article;
import com.zdx.service.zdx.ArticleService;
import com.zdx.mapper.zdx.ArticleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Service实现
* @createDate 2023-07-14 17:23:34
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchTrash(List<String> ids) {
        for (String id : ids) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Article::getTrash, Boolean.TRUE);
            updateWrapper.eq(Article::getId, id);
            if (!update(updateWrapper)) {
                return  false;
            }
        }
        return true;
    }
}




