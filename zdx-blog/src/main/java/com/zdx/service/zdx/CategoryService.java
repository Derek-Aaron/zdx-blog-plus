package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.front.CategoryTagArticleVo;
import com.zdx.model.vo.front.CategoryCountVo;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_category】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface CategoryService extends IService<Category> {

    /**
     * 分页查询分类
     * @param params 参数
     * @return 返回
     */
    IPage<Category> adminPage(RequestParams params);

    /**
     * 通过分类获取文章数量
     * @return 返回
     */
    List<CategoryCountVo> homeList();

    /**
     * 分页查询分类文章
     * @param params 请求参数
     * @return 返回
     */
    IPage<CategoryTagArticleVo> homeArticlePage(RequestParams params);
}
