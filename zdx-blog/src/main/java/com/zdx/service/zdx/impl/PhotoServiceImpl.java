package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Photo;
import com.zdx.service.zdx.PhotoService;
import com.zdx.mapper.zdx.PhotoMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_photo】的数据库操作Service实现
* @createDate 2023-07-14 17:23:35
*/
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo>
    implements PhotoService{

}




