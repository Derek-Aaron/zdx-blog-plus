package com.zdx.service.zdx.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Album;
import com.zdx.service.zdx.AlbumService;
import com.zdx.mapper.zdx.AlbumMapper;
import com.zdx.service.zdx.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【zdx_album】的数据库操作Service实现
* @createDate 2023-07-14 17:23:34
*/
@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
    implements AlbumService{

    private final PhotoService photoService;

    @Override
    public IPage<Album> pageAlbum(RequestParams params, Wrapper<Album> queryWrapper) {
        IPage<Album> page = new Page<>(params.getPage(), params.getLimit());
        IPage<Album> albumIPage = page(page, queryWrapper);
        for (Album album : albumIPage.getRecords()) {
            album.setPhotoCount(photoService.getPhotoCountByAlumId(album.getId()));
        }
        return albumIPage;
    }

    @Override
    public Album getAlbumById(String id) {
        Album album = getById(id);
        if (ObjUtil.isNotNull(album)) {
            album.setPhotoCount(photoService.getPhotoCountByAlumId(album.getId()));
        }
        return album;
    }
}




