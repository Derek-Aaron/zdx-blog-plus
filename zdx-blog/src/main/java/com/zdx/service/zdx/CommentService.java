package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.CommentPageVo;
import com.zdx.model.vo.front.CommentHomePageVo;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.model.vo.front.ReplyVo;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface CommentService extends IService<Comment> {

    /**
     * 查询看最新评论
     * @return 返回
     */
    List<RecentCommentVo> homeRecentComment();

    /**
     * 后台分页查询
     * @param params 请求参数
     * @return 返回
     */
    IPage<CommentPageVo> adminPage(RequestParams params);

    /**
     * 批量审核通过评论过
     * @param ids 评论id
     * @return 返回
     */
    boolean through(List<String> ids);

    /**
     * 分页查询评论
     * @param params 请求参数
     * @return 返回
     */
    IPage<CommentHomePageVo> homeCommentPage(RequestParams params);

    /**
     * 添加评论
     * @param comment 评论
     * @return 返回
     */
    boolean addComment(Comment comment);

    /**
     * 查询回复分页评论
     * @param params 请求参数
     * @return 返回
     */
    IPage<ReplyVo> replyPage(RequestParams params);
}
