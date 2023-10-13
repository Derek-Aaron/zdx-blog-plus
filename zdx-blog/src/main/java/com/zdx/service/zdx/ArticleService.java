package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.entity.zdx.Article;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.ArticleAdminVo;
import com.zdx.model.vo.ArticleSaveVo;
import com.zdx.model.vo.front.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_article】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface ArticleService extends IService<Article> {

    /**
     * 博客后台分页查询数据
     * @param params 参数
     * @return 返回 {@link com.zdx.model.vo.ArticleAdminVo}
     */
    IPage<ArticleAdminVo> adminPage(RequestParams params);

    /**
     * 后台保存文章
     * @param articleSave 文章实体
     * @return 返回
     */
    boolean adminSave(ArticleSaveVo articleSave);

    /**
     * 后台通过id查询文章
     * @param id 文章id
     * @return 返回
     */
    ArticleSaveVo adminGetById(String id);

    /**
     * 批量回收文章
     * @param ids 文章id
     * @return 是否成功
     */
    boolean batchTrash(List<String> ids);

    /**
     * 批量恢复文章数据
     * @param ids 数据id
     * @return 返回
     */
    boolean batchRecover(List<String> ids);

    /**
     * 前台进行分页
     * @param params 请求参数
     * @return 返回
     */
    IPage<ArticleHomeVo> homePage(RequestParams params);

    /**
     * 通过id查询文章详情
     * @param id id
     * @return 返回
     */
    ArticleHomeInfoVo getHomeById(String id);

    /**
     * 分页查询归档数据
     * @param params 请求参数
     * @return 返回
     */
    IPage<ArticleArchivesVo> archivesPage(RequestParams params);

    /**
     * 查询推荐文章
     * @return 返回
     */
    List<ArticleRecommendVo> homeRecommend();

    /**
     * 前台搜索文章
     * @param keyword 关键词
     * @return 返回
     */
    List<ArticleSearchVo> searchArticle(String keyword);

    /**
     * 手动同步es服务器
     * @param ids 文章id
     * @return 返回
     */
    boolean syncArticle(List<String> ids);

    /**
     * 文章导入
     * @param file 导入图片
     * @param context 内容
     * @return 返回文章内容
     */
    String articleUpload(MultipartFile[] files, String content) throws IOException;
}
