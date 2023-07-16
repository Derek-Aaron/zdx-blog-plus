package com.zdx.mapper.zdx;

import com.zdx.entity.zdx.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Mapper
* @createDate 2023-07-14 17:23:35
* @Entity com.zdx.entity.zdx.Tag
*/
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 通过标签id获取文章id
     * @param tagId 标签id
     * @return 返回
     */
    List<String> getArticleIdByTagId(@Param("tagId") String tagId);

    int getCountByArticleIdAndTagId(@Param("articleId") Long articleId,@Param("tagId") Long tagId);

    void insertArticleTag(@Param("articleId") Long articleId,@Param("tagId") Long tagId);

    List<Tag> getTagByArticleId(@Param("articleId") Long articleId);
}




