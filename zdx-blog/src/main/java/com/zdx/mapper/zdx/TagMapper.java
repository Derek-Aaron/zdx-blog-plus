package com.zdx.mapper.zdx;

import com.zdx.entity.zdx.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Mapper
* @createDate 2023-07-17 16:51:40
* @Entity com.zdx.entity.zdx.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 通过标签id查询文章id
     * @param tagId 标签id
     * @return 返回
     */
    List<String> getArticleIdByTagId(@Param("tagId") String tagId);

    /**
     * 通过文章id获取标签
     * @param articleId 文章id
     * @return 返回
     */
    List<Tag> getTagByArticleId(@Param("articleId") Long articleId);

    /**
     * 保存标签与文章的关联
     *
     * @param articleId 文章id
     * @param tagIds    标签ids
     */
    void insertTagAndArticleId(@Param("articleId") Long articleId, @Param("tagIds") List<Long> tagIds);

    /**
     * 通过标签id查询是否包含标签与文章的关联
     * @param articleId 文章id
     * @param tagId 标签id
     * @return 返回
     */
    Long selectCountTagIdAndArticleId(@Param("articleId") Long articleId,@Param("tagId") Long tagId);

    /**
     * 通过标签id查询文章
     * @param tagId 标签id
     * @return 返回
     */
    Long selectArticleCountByTagId(@Param("tagId") Long tagId);
}




