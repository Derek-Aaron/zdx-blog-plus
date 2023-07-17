package com.zdx.mapper.zdx;

import com.zdx.controller.dto.RequestParams;
import com.zdx.controller.vo.CommentHomeVo;
import com.zdx.controller.vo.ReplyCountVO;
import com.zdx.controller.vo.ReplyVO;
import com.zdx.entity.zdx.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Mapper
* @createDate 2023-07-14 17:23:35
* @Entity com.zdx.entity.zdx.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 通过typeId分页查询评论
     * @param params 参数
     * @return 返回
     */
    List<CommentHomeVo> selectParentComment(@Param("offset")Long offset, @Param("params") RequestParams params);

    /**
     * 通过id查询子级评论
     * @param parentCommentIdList 评论id
     * @return 返回
     */
    List<ReplyVO> selectReplyByParentIdList(@Param("commentIds") List<Long> parentCommentIdList);

    /**
     * 通过评论id查询回复数
     * @param parentCommentIdList 评论id
     * @return 返回
     */
    List<ReplyCountVO> selectReplyCountByParentId(@Param("commentIds") List<Long> parentCommentIdList);
}




