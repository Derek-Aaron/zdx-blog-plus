package com.zdx.service.zdx.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Comment;
import com.zdx.mapper.zdx.CommentMapper;
import com.zdx.service.us.UserService;
import com.zdx.service.zdx.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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




