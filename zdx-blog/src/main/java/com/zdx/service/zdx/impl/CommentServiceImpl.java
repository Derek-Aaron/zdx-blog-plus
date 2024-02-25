package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.entity.tk.Config;
import com.zdx.entity.us.User;
import com.zdx.entity.zdx.Article;
import com.zdx.entity.zdx.Comment;
import com.zdx.entity.zdx.Talk;
import com.zdx.enums.CommentTypeEnum;
import com.zdx.mapper.tk.ConfigMapper;
import com.zdx.mapper.us.UserMapper;
import com.zdx.mapper.zdx.ArticleMapper;
import com.zdx.mapper.zdx.CommentMapper;
import com.zdx.mapper.zdx.TalkMapper;
import com.zdx.model.dto.MailDto;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.CommentPageVo;
import com.zdx.model.vo.front.*;
import com.zdx.security.UserSessionFactory;
import com.zdx.service.tk.EmailService;
import com.zdx.service.zdx.CommentService;
import com.zdx.utils.MybatisPlusUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author zhaodengxuan
 * @description 针对表【zdx_comment】的数据库操作Service实现
 * @createDate 2023-07-17 16:51:40
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService {

    private final UserMapper userMapper;

    private final ConfigMapper configMapper;

    private final ArticleMapper articleMapper;

    private final TalkMapper talkMapper;

    private final EmailService emailService;

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
        queryWrapper.orderByDesc(Comment::getCreateTime);
        Page<Comment> page = page(new Page<>(params.getPage(), params.getLimit()), queryWrapper);
        List<CommentPageVo> commentPageVos = new ArrayList<>();
        for (Comment comment : page.getRecords()) {
            CommentPageVo commentPageVo = BeanUtil.copyProperties(comment, CommentPageVo.class);
            User fromUser = userMapper.selectById(comment.getFromUid());
            if (ObjUtil.isNotNull(fromUser)) {
                commentPageVo.setFromName(StrUtil.isNotBlank(fromUser.getNickname()) ? fromUser.getNickname() : fromUser.getUsername());
            }
            User toUser = userMapper.selectById(comment.getToUid());
            if (ObjUtil.isNotNull(toUser)) {
                commentPageVo.setToName(StrUtil.isNotBlank(toUser.getNickname()) ? toUser.getNickname() : toUser.getUsername());
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
        queryWrapper.orderByDesc(Comment::getCreateTime);
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
                commentHomePageVo.setFromNickname(StrUtil.isNotBlank(fromUser.getNickname()) ? fromUser.getNickname() : fromUser.getUsername());
                commentHomePageVo.setAvatar(fromUser.getAvatar());
            }
            commentHomePageVo.setReplyVoList(replyMap.get(comment.getId()));
            commentHomePageVo.setReplyCount(Optional.ofNullable(replyCountMap.get(comment.getId())).orElse(0L));
            commentHomePageVos.add(commentHomePageVo);
        }
        return MybatisPlusUtils.pageConvert(page, commentHomePageVos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addComment(Comment comment) {
        comment.setFromUid(UserSessionFactory.getUserId());
        if (saveOrUpdate(comment)) {
            Config config = configMapper.selectOne(new LambdaQueryWrapper<Config>()
                    .eq(Config::getName, Constants.BLOG_SITE_CONFIG)
            );
            if (ObjUtil.isNotNull(config)) {
                SiteConfig siteConfig = JSON.parseObject(config.getValue(), SiteConfig.class);
                if (Boolean.TRUE.equals(siteConfig.getEmailNotice())) {
                    String nickname = UserSessionFactory.getNickname();
                    CompletableFuture.runAsync(() -> {
                        notice(comment, nickname, siteConfig);
                    });
                }
            }
            return true;
        }
        return false;
    }

    private void notice(Comment comment, String nickname, SiteConfig siteConfig) {
//        if (comment.getFromUid().equals(comment.getToUid())) {
//            return;
//        }
        // 邮件标题
        String title = "友链";
        // 回复用户id
        Long toUid = Constants.BLOGGER_ID;

        if (ObjUtil.isNull(comment.getParentId())) {
            if (comment.getCommentType().equals(CommentTypeEnum.ARTICLE.name())) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .select(Article::getTitle, Article::getId, Article::getUserId)
                        .eq(Article::getId, comment.getTypeId())
                );
                if (ObjUtil.isNotNull(article)) {
                    title = article.getTitle();
                    toUid = article.getUserId();
                }
            }
            if (comment.getCommentType().equals(CommentTypeEnum.TALK.name())) {
                title = "说说";
                Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                        .select(Talk::getUserId, Talk::getId)
                        .eq(Talk::getId, comment.getTypeId())
                );
                if (ObjUtil.isNotNull(talk)) {
                    toUid = talk.getUserId();
                }
            }
//            if(comment.getFromUid().equals(toUid)){
//                return;
//            }
        } else {
            toUid = comment.getToUid();
            if (comment.getCommentType().equals(CommentTypeEnum.ARTICLE.name())) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .select(Article::getTitle, Article::getId)
                        .eq(Article::getId, comment.getTypeId())
                );
                if (ObjUtil.isNotNull(article)) {
                    title = article.getTitle();
                }
            }
            if (comment.getCommentType().equals(CommentTypeEnum.TALK.name())) {
                title = "说说";
            }
        }
        User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail, User::getNickname)
                .eq(User::getId, toUid));
        if (StrUtil.isNotBlank(toUser.getEmail())) {
            sendEmail(comment, toUser, title, nickname, siteConfig);
        }
    }

    private void sendEmail(Comment comment, User toUser, String title, String nickname, SiteConfig siteConfig) {
        MailDto mailDTO = MailDto.builder().build();
        Map<String, Object> contentMap = new HashMap<>(7);
        String typeId = Optional.ofNullable(comment.getTypeId())
                .map(Object::toString)
                .orElse("");

        String url = siteConfig.getSiteAddress() + "/" + comment.getCommentType().toLowerCase(Locale.ROOT) + "/" + typeId;
        if (ObjUtil.isNull(comment.getParentId())) {
            mailDTO.setToEmail(toUser.getEmail());
            mailDTO.setSubject(Constants.COMMENT_REMIND);
            mailDTO.setTemplate(Constants.AUTHOR_TEMPLATE);
            contentMap.put("time", DateUtil.formatDate(comment.getCreateTime()));
            contentMap.put("url", url);
            contentMap.put("title", title);
            contentMap.put("nickname", nickname);
            contentMap.put("content", comment.getContent());
            mailDTO.setContentMap(contentMap);
        } else {
            Comment parentComment = baseMapper.selectOne(new LambdaQueryWrapper<Comment>()
                    .select(Comment::getContent, Comment::getCreateTime)
                    .eq(Comment::getId, comment.getReplyId()));

            mailDTO.setToEmail(toUser.getEmail());
            mailDTO.setSubject(Constants.COMMENT_REMIND);
            mailDTO.setTemplate(Constants.USER_TEMPLATE);
            contentMap.put("url", url);
            contentMap.put("title", title);
            contentMap.put("time", DateUtil.formatDate(comment.getCreateTime()));
            // 被回复用户昵称
            contentMap.put("toUser", toUser.getNickname());
            // 评论用户昵称
            contentMap.put("fromUser", nickname);
            // 被回复的评论内容
            contentMap.put("parentComment", parentComment.getContent());
            // 回复评论内容
            contentMap.put("replyComment", comment.getContent());
            mailDTO.setContentMap(contentMap);
        }
        emailService.sendHtml(mailDTO);
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




