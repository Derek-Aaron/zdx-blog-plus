package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Article;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Service
* @createDate 2023-07-14 17:23:34
*/
public interface ArticleService extends IService<Article> {

    /**
     * 批量回收文章
     * @param ids 文章id
     * @return 成功
     */
    boolean batchTrash(List<String> ids);

    /**
     * 保存文章内容
     * @param data 文章
     * @return 成功
     */
    boolean saveArticle(Article data);

    /**
     * 分页查询博客
     * @param params 请求参数
     * @param queryWrapper 查询条件
     * @return 返回
     */
    IPage<Article> pageArticle(RequestParams params, Wrapper<Article> queryWrapper);

    /**
     * 通过id获取文章
     * @param id 文章id
     * @return 返回
     */
    Article getArticleById(String id);

    /**
     * 增加博客访问量
     * @param id 博客id
     * @return 返回
     */
    boolean addView(String id);
}
