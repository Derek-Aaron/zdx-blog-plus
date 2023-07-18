package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Tag;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.TagService;
import com.zdx.mapper.zdx.TagMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public void insertTagAndArticleId(Long articleId, List<Long> tagIds) {
        baseMapper.insertTagAndArticleId(articleId, tagIds);
    }

    @Override
    public Long selectCountTagIdAndArticleId(Long articleId, Long tagId) {
        return baseMapper.selectCountTagIdAndArticleId(articleId, tagId);
    }

    @Override
    public IPage<Tag> adminTag(RequestParams params) {
        IPage<Tag> iPage = new Page<>(params.getPage(), params.getLimit());
        return page(iPage, new LambdaQueryWrapper<Tag>()
                .like(params.hasParam("name"), Tag::getName, params.getParam("name")));
    }
}




