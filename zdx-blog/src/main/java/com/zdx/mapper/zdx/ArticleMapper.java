package com.zdx.mapper.zdx;

import com.zdx.entity.zdx.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdx.model.vo.ArticleRankVO;
import com.zdx.model.vo.ArticleStatisticsVO;
import com.zdx.model.vo.front.ArticlePaginationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Mapper
* @createDate 2023-07-17 16:51:40
* @Entity com.zdx.entity.zdx.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询博客的上一篇文章
     * @param articleId 博客id
     * @return 返回
     */
    ArticlePaginationVO selectLastArticle(@Param("articleId") Long articleId);

    /**
     * 获取博客的下一篇文章
     * @param articleId 博客id
     * @return 返回
     */
    ArticlePaginationVO selectNextArticle(@Param("articleId") Long articleId);

    /**
     * 通过文章发布
     * @return 返回
     */
    List<ArticleStatisticsVO> selectArticleStatistics();

    /**
     * 文章浏览量排行
     * @return 返回
     */
    List<ArticleRankVO> selectArticleRank();
}




