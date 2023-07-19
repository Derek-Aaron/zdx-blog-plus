package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Comment;
import com.zdx.mapper.us.UserMapper;
import com.zdx.mapper.zdx.CommentMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.CommentPageVo;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.service.zdx.CommentService;
import com.zdx.utils.MybatisPlusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    private final UserMapper userMapper;
    @Override
    public List<RecentCommentVo> homeRecentComment() {
        return baseMapper.selectRecentComment();
    }

    @Override
    public IPage<CommentPageVo> adminPage(RequestParams params) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        if (params.hasParam("nickname")) {
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.select(User::getId);
            userQueryWrapper.like(User::getNickname, params.getParam("nickname"));
            queryWrapper.or();
            userQueryWrapper.like(User::getUsername, params.getParam("username"));
            List<Long> userIds = userMapper.selectList(userQueryWrapper).stream().map(User::getId).toList();
            queryWrapper.in(Comment::getFromUid, userIds);
        }
        queryWrapper.eq(params.hasParam("isCheck"), Comment::getIsCheck, params.getParam("isCheck", Boolean.class));
        queryWrapper.eq(params.hasParam("commentType"), Comment::getCommentType, params.getParam("commentType"));
        Page<Comment> page = page(new Page<>(params.getPage(), params.getLimit()), queryWrapper);
        List<CommentPageVo> commentPageVos = new ArrayList<>();
        for (Comment comment : page.getRecords()) {
            CommentPageVo commentPageVo = BeanUtil.copyProperties(comment, CommentPageVo.class);
            User fromUser = userMapper.selectById(comment.getFromUid());
            if (ObjUtil.isNotNull(fromUser)) {
                commentPageVo.setFromName(StrUtil.isNotBlank(fromUser.getNickname()) ? fromUser.getNickname(): fromUser.getUsername());
            }
            User toUser = userMapper.selectById(comment.getToUid());
            if (ObjUtil.isNotNull(toUser)) {
                commentPageVo.setToName(StrUtil.isNotBlank(toUser.getNickname()) ? toUser.getNickname(): toUser.getUsername());
            }
            commentPageVos.add(commentPageVo);
        }
        return MybatisPlusUtils.pageConvert(page, commentPageVos);
    }

    @Override
    public boolean through(List<String> ids) {
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Comment::getIsCheck, Boolean.TRUE);
        updateWrapper.in(Comment::getId, ids);
        return update(updateWrapper);
    }
}




