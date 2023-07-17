package com.zdx.service.zdx.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.controller.dto.RequestParams;
import com.zdx.controller.vo.CommentHomeVo;
import com.zdx.controller.vo.ReplyCountVO;
import com.zdx.controller.vo.ReplyVO;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Comment;
import com.zdx.mapper.zdx.CommentMapper;
import com.zdx.service.RedisService;
import com.zdx.service.us.UserService;
import com.zdx.service.zdx.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Service实现
* @createDate 2023-07-14 17:23:34
*/
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    private final UserService userService;

    private final RedisService redisService;

    @Override
    public IPage<Comment> pageCommentVo(RequestParams params, Wrapper<Comment> wrapper) {
        IPage<Comment> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Comment> commentIPage = page(iPage, wrapper);
        for (Comment record : commentIPage.getRecords()) {
            String fromName = getUserName(record.getFromUid());
            record.setFromName(fromName);
            String toName = getUserName(record.getToUid());
            record.setToName(toName);
        }
        return commentIPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean through(List<String> ids) {
        for (String id : ids) {
            LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(Comment::getIsCheck, Boolean.TRUE);
            updateWrapper.eq(Comment::getId, id);
            if (!update(updateWrapper)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IPage<CommentHomeVo> pageHomeCommentVo(RequestParams params) {
        IPage<CommentHomeVo> page = new Page<>(params.getPage(), params.getLimit());
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<Comment>()
                .eq(params.hasParam("typeId"), Comment::getTypeId, params.getParam("typeId"))
                .eq(Comment::getCommentType, params.getParam("commentType"))
                .eq(Comment::getIsCheck, Boolean.TRUE).isNull(Comment::getParentId);
        Long count = baseMapper.selectCount(queryWrapper);
        if (count == 0) {
            return page;
        }
        List<CommentHomeVo> commentHomeVos = baseMapper.selectParentComment(params.getOffset(), params);
        if (CollectionUtils.isEmpty(commentHomeVos)) {
            return page;
        }
        Map<String, Integer> likeCountMap = redisService.getHashAll(Constants.COMMENT_LIKE_COUNT);
        List<Long> parentCommentIdList = commentHomeVos.stream().map(CommentHomeVo::getId).toList();
        List<ReplyVO> replyVOList = baseMapper.selectReplyByParentIdList(parentCommentIdList);
        replyVOList.forEach(item -> item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0)));
        Map<Long, List<ReplyVO>> replyMap = replyVOList.stream().collect(Collectors.groupingBy(ReplyVO::getParentId));
        List<ReplyCountVO> replyCountList = baseMapper.selectReplyCountByParentId(parentCommentIdList);
        Map<Long, Integer> replyCountMap = replyCountList.stream().collect(Collectors.toMap(ReplyCountVO::getCommentId, ReplyCountVO::getReplyCount));
        commentHomeVos.forEach(item -> {
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            item.setReplyVOList(replyMap.get(item.getId()));
            item.setReplyCount(Optional.ofNullable(replyCountMap.get(item.getId())).orElse(0));
        });
        page.setRecords(commentHomeVos);
        page.setTotal(count);
        return page;
    }

    private String getUserName(Long userId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getId, userId);
        queryWrapper.select(User::getNickname, User::getUsername);
        User user = userService.getOne(queryWrapper);
        if (ObjUtil.isNotNull(user)) {
            if (StrUtil.isNotBlank(user.getNickname())) {
                return user.getNickname();
            }
            if (StrUtil.isNotBlank(user.getUsername())) {
                return user.getUsername();
            }
        }
        return null;
    }
}




