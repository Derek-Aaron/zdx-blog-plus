package com.zdx.search;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;

public interface SearchTemplate {

    /**
     * 创建索引
     * @param index 索引
     * @return 返回
     */
    Boolean createIndex(String index);

    /**
     * 删除索引
     * @param index 索引
     * @return 返回
     */
    Boolean deleteIndex(String index);

    /**
     * 判断索引是否存在
     * @param index 索引
     * @return 返回
     */
    Boolean isIndexExist(String index);


    /**
     * 添加文档
     *
     * @param object 值
     * @param index  索引
     * @param id     id
     */
    void insertDoc(Object object, String index, String id);


    /**
     * 通过id删除文档
     * @param index 索引
     * @param id id
     */
    void deleteDoc(String index, String id);

    /**
     * 通过id更新文档
     * @param data 内容
     * @param index 索引
     * @param id id
     */
    void updateDoc(Object data, String index, String id);

    /**
     * 通过id获取文档
     * @param index 索引
     * @param id id
     * @return 返回
     */
    <T> T getDocById(String index, String id, Class<T> clazz);

    /**
     * 通过id判断文档是否存在
     * @param index 索引
     * @param id id
     * @return 返回
     */
    Boolean existsDocById(String index, String id);

    /**
     * 批量插入
     * @param index 索引
     * @param request 请求
     */
    Boolean bulkDoc(String index, BulkRequest request);

    /**
     * 查询全部
     * @param index 索引
     * @param clazz 映射类
     * @return 返回
     * @param <T> 实体
     */
    <T> List<T> searchAll(String index, Class<T> clazz);

    /**
     * 查询文档高亮显示
     * @param index 返回
     * @param query 查询
     * @param clazz 映射类
     * @param highlightField 高亮此段
     * @return 返回
     * @param <T> 返回实体
     */
    <T> List<T> searchQuery(String index, SearchSourceBuilder query, Class<T> clazz, String... highlightField);

    /**
     * es 搜索文档
     * @param index 索引
     * @param query 查询条件
     * @param limit 分页大小
     * @param page 分页
     * @param sortField 排序字段
     * @param highlightField 高亮字段
     * @param clazz 映射类class
     * @param fields 查询字段
     * @return 返回
     * @param <T> 实体
     */

    @SuppressWarnings("all")
    <T> IPage<T> searchDoc(String index,
                    SearchSourceBuilder query,
                    Integer limit,
                    Integer page,
                    SortBuilder[] sortField,
                    String[] highlightField, Class<T> clazz, String... fields);
}
