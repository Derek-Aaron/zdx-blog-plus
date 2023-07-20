package com.zdx.event.handle.zdx;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zdx.entity.zdx.Article;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.service.zdx.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InsertArticleViewCountEvent implements EventHandle {


    private final ArticleService articleService;
    @Override
    public String getKey() {
        return EventObject.Attribute.INSERT_VIEW_COUNT;
    }

    @Override
    public void invokeEvent(EventObject event) {
        Long articleId = event.getSource(Long.class);
        Long viewCount = event.getAttribute(EventObject.Attribute.VIEW_COUNT, Long.class);
        if (ObjUtil.isNotNull(articleId) && ObjUtil.isNotNull(viewCount)) {
            viewCount++;
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Article::getViewCount, viewCount);
            updateWrapper.eq(Article::getId, articleId);
            articleService.update(updateWrapper);
        }
    }
}
