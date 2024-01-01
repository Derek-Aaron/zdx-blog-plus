package com.zdx.service.zdx.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Album;
import com.zdx.entity.zdx.Photo;
import com.zdx.mapper.zdx.AlbumMapper;
import com.zdx.mapper.zdx.PhotoMapper;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.AlbumPhotoCountVo;
import com.zdx.model.vo.front.PhotoInfoVo;
import com.zdx.service.zdx.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_album】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
@RequiredArgsConstructor
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album>
    implements AlbumService{

    private final PhotoMapper photoMapper;
    @Override
    public IPage<AlbumPhotoCountVo> adminPage(RequestParams params) {
        IPage<Album> iPage = new Page<>(params.getPage(), params.getLimit());
        IPage<Album> page = page(iPage, new LambdaQueryWrapper<Album>()
                .like(params.hasParam("name"), Album::getName, params.getParam("name")));
        List<AlbumPhotoCountVo> albumPageVos = new ArrayList<>();
        for (Album album : page.getRecords()) {
            Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>().eq(Photo::getAlbumId, album.getId()));
            AlbumPhotoCountVo albumPageVo = BeanUtil.copyProperties(album, AlbumPhotoCountVo.class);
            albumPageVo.setPhotoCount(count);
            albumPageVos.add(albumPageVo);
        }
        IPage<AlbumPhotoCountVo> albumPageVoIPage = new Page<>(params.getPage(), params.getLimit());
        albumPageVoIPage.setPages(page.getPages());
        albumPageVoIPage.setCurrent(page.getCurrent());
        albumPageVoIPage.setSize(page.getSize());
        albumPageVoIPage.setTotal(page.getTotal());
        albumPageVoIPage.setRecords(albumPageVos);
        return albumPageVoIPage;
    }

    @Override
    public AlbumPhotoCountVo getAlbumPhotoCountById(String id) {
        Album album = getById(id);
        AlbumPhotoCountVo albumPhotoCountVo = null;
        if (ObjUtil.isNotNull(album)) {
            Long count = photoMapper.selectCount(new LambdaQueryWrapper<Photo>().eq(Photo::getAlbumId, album.getId()));
            albumPhotoCountVo = BeanUtil.copyProperties(album, AlbumPhotoCountVo.class);
            albumPhotoCountVo.setPhotoCount(count);
        }
        return albumPhotoCountVo;
    }

    @Override
    public PhotoInfoVo homePhotoList(String albumId) {
        Album album = getById(albumId);
        PhotoInfoVo photoInfoVo = new PhotoInfoVo();
        if (ObjUtil.isNotNull(album)) {
            photoInfoVo.setAlbumName(album.getName());
            photoInfoVo.setAlbumCover(album.getCover());
        }
        List<Photo> photos = photoMapper.selectList(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId));
        photoInfoVo.setPhotoVOList(photos);
        return photoInfoVo;
    }
}




