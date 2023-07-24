package com.zdx.event.handle;

import com.zdx.event.EventHandle;
import com.zdx.event.EventObject;
import com.zdx.model.vo.ArticleEsVo;
import com.zdx.search.SearchTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("'${zdx.es.open}' == 'true'")
public class SaveOrUpdateArticleEsEvent implements EventHandle {

    private final SearchTemplate searchTemplate;

    @Value("${zdx.es.index}")
    private String index;
    @Override
    public String getKey() {
        return EventObject.Attribute.ES_CREATE_UPDATE;
    }

    @Override
    public void invokeEvent(EventObject event) {
        ArticleEsVo articleEsVo = event.getSource(ArticleEsVo.class);
        String articleId = String.valueOf(articleEsVo.getId());
        if (searchTemplate.existsDocById(index, articleId)) {
            searchTemplate.updateDoc(articleEsVo, index, articleId);
        } else {
            searchTemplate.insertDoc(articleEsVo, index, articleId);
        }
    }
}
