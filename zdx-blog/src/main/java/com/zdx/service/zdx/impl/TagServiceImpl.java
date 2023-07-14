package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Tag;
import com.zdx.service.zdx.TagService;
import com.zdx.mapper.zdx.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_tag】的数据库操作Service实现
* @createDate 2023-07-14 17:23:35
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




