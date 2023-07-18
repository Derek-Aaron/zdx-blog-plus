package com.zdx.event.handle;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zdx.entity.zdx.Tag;
import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.service.zdx.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveOrUpdateTagEvent implements EventHandle {

    private final TagService tagService;
    @Override
    public String getKey() {
        return EventObject.Attribute.SAVEORUPDATETAGS;
    }

    @Override
    public void invokeEvent(EventObject event) {
        List<String> tagNames = event.getSource(List.class);
        Long articleId = event.getAttribute(EventObject.Attribute.ARTICLE_ID, Long.class);
        List<Long> tagIds = new ArrayList<>();
        if (ObjUtil.isNotNull(articleId) && ObjUtil.isNotNull(tagNames)) {
            for (String tagName : tagNames) {
                Tag tag = tagService.getOne(new LambdaQueryWrapper<Tag>().select(Tag::getId).eq(Tag::getName, tagName));
                if (ObjUtil.isNull(tag)) {
                    tag = new Tag();
                    tag.setName(tagName);
                    tagService.saveOrUpdate(tag);
                }
                Long count = tagService.selectCountTagIdAndArticleId(articleId, tag.getId());
                if (!(ObjUtil.isNotNull(count) && count > 0)) {
                    tagIds.add(tag.getId());
                }
            }
        }
        tagService.insertTagAndArticleId(articleId, tagIds);
    }
}
