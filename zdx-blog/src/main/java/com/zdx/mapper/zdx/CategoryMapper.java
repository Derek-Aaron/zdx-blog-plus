package com.zdx.mapper.zdx;

import com.zdx.entity.zdx.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdx.model.vo.front.CategoryCountVo;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_category】的数据库操作Mapper
* @createDate 2023-07-17 16:51:40
* @Entity com.zdx.entity.zdx.Category
*/
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询每个分类下买呢文章的数量
     * @return 返回
     */
    List<CategoryCountVo> selectCategoryCountVo();
}




