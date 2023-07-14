package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Service
* @createDate 2023-07-14 17:23:35
*/
public interface CommentService extends IService<Comment> {

    /**
     * 分页查询评论过
     * @param params 请求参数
     * @param wrapper 查询条件
     * @return 返回
     */
    IPage<Comment> pageCommentVo(RequestParams params, Wrapper<Comment> wrapper);

    /**
     * 批量审核评论
     * @param ids 评论id
     * @return 成功
     */
    boolean through(List<String> ids);
}
