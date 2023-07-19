package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.TagCountVo;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface TagService extends IService<Tag> {

    /**
     * 保存标签与文章的关联
     *
     * @param articleId 文章id
     * @param tagIds    标签ids
     */
    void insertTagAndArticleId(Long articleId, List<Long> tagIds);

    /**
     * 通过标签id查询是否包含标签与文章的关联
     * @param articleId 文章id
     * @param tagId 标签id
     * @return 返回
     */
    Long selectCountTagIdAndArticleId(Long articleId, Long tagId);

    /**
     * 后台分页查询标签
     * @param params 查询参数
     * @return 返回
     */
    IPage<Tag> adminTag(RequestParams params);

    /**
     * 标签查询
     * @return 返回
     */
    List<TagCountVo> homeList();

    /**
     * 通过标签查询文章
     * @param params 请求参数
     * @return 返回
     */
    IPage<CategoryTagArticleVo> homeArticlePage(RequestParams params);
}
