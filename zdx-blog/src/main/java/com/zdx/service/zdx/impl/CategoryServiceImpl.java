package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Category;
import com.zdx.mapper.zdx.CategoryMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.CategoryService;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_category】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Override
    public IPage<Category> adminPage(RequestParams params) {
        IPage<Category> iPage = new Page<>(params.getPage(), params.getLimit());

        return page(iPage, new LambdaQueryWrapper<Category>()
                .like(params.hasParam("name"), Category::getName, params.getParam("name")));
    }
}




