package com.zdx.mapper.zdx;

import com.zdx.entity.zdx.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdx.model.vo.front.RecentCommentVo;

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
}




