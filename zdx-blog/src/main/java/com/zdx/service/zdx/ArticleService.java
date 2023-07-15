package com.zdx.service.zdx;

import com.zdx.entity.zdx.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Service
* @createDate 2023-07-14 17:23:34
*/
public interface ArticleService extends IService<Article> {

    /**
     * 批量回收文章
     * @param ids 文章id
     * @return 成功
     */
    boolean batchTrash(List<String> ids);

}
