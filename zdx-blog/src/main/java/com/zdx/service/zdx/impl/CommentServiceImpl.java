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
import com.zdx.model.vo.front.CommentHomePageVo;
import com.zdx.model.vo.front.RecentCommentVo;
import com.zdx.model.vo.front.ReplyCountVO;
import com.zdx.model.vo.front.ReplyVo;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.zdx.CommentService;
import com.zdx.utils.MybatisPlusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public IPage<CommentHomePageVo> homeCommentPage(RequestParams params) {
        IPage<Comment> iPage = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentType, params.getParam("commentType"));
        queryWrapper.eq(Comment::getTypeId, params.getParam("typeId"));
        queryWrapper.eq(Comment::getIsCheck, Boolean.TRUE);
        queryWrapper.isNull(Comment::getParentId);
        IPage<Comment> page = page(iPage, queryWrapper);
        if (page.getRecords().isEmpty()) {
            return new Page<>(params.getPage(), params.getLimit());
        }
        //获取父级评论id
        List<Long> commentIds = page.getRecords().stream().map(Comment::getId).toList();
        List<ReplyVo> replyVOS = baseMapper.selectReplyByParentIdList(commentIds);
        Map<Long, List<ReplyVo>> replyMap = replyVOS.stream().collect(Collectors.groupingBy(ReplyVo::getParentId));
        List<ReplyCountVO> replyCountList = baseMapper.selectReplyCountByParentId(commentIds);
        Map<Long, Long> replyCountMap = replyCountList.stream().collect(Collectors.toMap(ReplyCountVO::getCommentId, ReplyCountVO::getReplyCount));
       List<CommentHomePageVo> commentHomePageVos = new ArrayList<>();
        for (Comment comment : page.getRecords()) {
            CommentHomePageVo commentHomePageVo = BeanUtil.copyProperties(comment, CommentHomePageVo.class);
            User fromUser = userMapper.selectById(comment.getFromUid());
            if (ObjUtil.isNotNull(fromUser)) {
                commentHomePageVo.setFromNickname(StrUtil.isNotBlank(fromUser.getNickname()) ? fromUser.getNickname(): fromUser.getUsername());
                commentHomePageVo.setAvatar(fromUser.getAvatar());
            }
            commentHomePageVo.setReplyVoList(replyMap.get(comment.getId()));
            commentHomePageVo.setReplyCount(Optional.ofNullable(replyCountMap.get(comment.getId())).orElse(0L));
            commentHomePageVos.add(commentHomePageVo);
        }
        return MybatisPlusUtils.pageConvert(page, commentHomePageVos);
    }

    @Override
    public boolean addComment(Comment comment) {
        comment.setFromUid(UserSessionFactory.getUserId());
        return save(comment);
    }

    @Override
    public IPage<ReplyVo> replyPage(RequestParams params) {
        IPage<Comment> iPage = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, params.getParam("commentId"));
        IPage<Comment> page = page(iPage, queryWrapper);
        if (page.getTotal() == 0) {
            return new Page<>();
        }
        List<ReplyVo> replyVos = new ArrayList<>();
        for (Comment comment : page.getRecords()) {
            ReplyVo replyVo = BeanUtil.copyProperties(comment, ReplyVo.class);
            User fromUser = userMapper.selectById(comment.getFromUid());
            if (ObjUtil.isNotNull(fromUser)) {
                replyVo.setAvatar(fromUser.getAvatar());
                replyVo.setFromNickname(StrUtil.isNotBlank(fromUser.getNickname()) ? fromUser.getNickname() : fromUser.getUsername());
            }
            User toUser = userMapper.selectById(comment.getToUid());
            if (ObjUtil.isNotNull(toUser)) {
                replyVo.setToNickname(StrUtil.isNotBlank(toUser.getNickname()) ? toUser.getNickname() : toUser.getUsername());
            }
            replyVos.add(replyVo);
        }

        return MybatisPlusUtils.pageConvert(page, replyVos);
    }
}




