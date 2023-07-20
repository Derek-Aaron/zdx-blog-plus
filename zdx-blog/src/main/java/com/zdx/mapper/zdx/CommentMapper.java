package com.zdx.mapper.zdx;

import com.zdx.entity.zdx.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.model.vo.front.ReplyCountVO;
import com.zdx.model.vo.front.ReplyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Mapper
* @createDate 2023-07-17 16:51:40
* @Entity com.zdx.entity.zdx.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * 查询最新评论
     * @return 返回
     */
    List<RecentCommentVo> selectRecentComment();

    /**
     * 分组查询评论回复默认三条
     * @param commentIds 评论id
     * @return 返回
     */
    List<ReplyVo> selectReplyByParentIdList(@Param("commentIds") List<Long> commentIds);

    /**
     * 分组查询父评论的回复数
     * @param commentIds 评论id
     * @return 返回
     */
    List<ReplyCountVO> selectReplyCountByParentId(@Param("commentIds") List<Long> commentIds);
}




