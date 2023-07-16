package com.zdx.mapper.zdx;

import com.zdx.controller.vo.ArticlePaginationVO;
import com.zdx.entity.zdx.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Mapper
* @createDate 2023-07-14 17:23:34
* @Entity com.zdx.entity.zdx.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询一条记录的上一条记录
     * @param articleId 记录id
     * @return 返回
     */
    ArticlePaginationVO selectLastArticle(@Param("articleId") String articleId);

    /**
     * 查询一条记录的下一条记录
     * @param id 文章id
     * @return 返回
     */
    ArticlePaginationVO selectNextArticle(String id);
}




