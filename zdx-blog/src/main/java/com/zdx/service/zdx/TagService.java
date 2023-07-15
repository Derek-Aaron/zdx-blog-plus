package com.zdx.service.zdx;

import com.zdx.entity.zdx.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Service
* @createDate 2023-07-14 17:23:35
*/
public interface TagService extends IService<Tag> {

    /**
     * 通过标签id获取文章id
     * @param tagId 标签is
     * @return 返回
     */
    List<String> getArticleIdByTagId(String tagId);
}
