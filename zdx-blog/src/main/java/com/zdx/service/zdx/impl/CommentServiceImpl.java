package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Comment;
import com.zdx.service.zdx.CommentService;
import com.zdx.mapper.zdx.CommentMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_comment】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




