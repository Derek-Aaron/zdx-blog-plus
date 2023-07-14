package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Album;
import com.zdx.service.zdx.AlbumService;
import com.zdx.mapper.zdx.AlbumMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_album】的数据库操作Service实现
* @createDate 2023-07-14 17:23:34
*/
@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
    implements AlbumService{

}




