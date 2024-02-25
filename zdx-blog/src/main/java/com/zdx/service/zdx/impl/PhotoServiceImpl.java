package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.zdx.Photo;
import com.zdx.model.dto.PhotoAddDto;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.PhotoService;
import com.zdx.mapper.zdx.PhotoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_photo】的数据库操作Service实现
* @createDate 2023-07-17 16:51:40
*/
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo>
    implements PhotoService{

    @Override
    public IPage<Photo> adminPage(RequestParams params) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, params.getParam("albumId"));
        queryWrapper.orderByDesc(Photo::getCreateTime);
        IPage<Photo> iPage = new Page<>(params.getPage(), params.getLimit());
        return page(iPage, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPhoto(PhotoAddDto photoAddDto) {
        List<Photo> photos = new ArrayList<>();
        for (String url : photoAddDto.getPhotoUrlList()) {
            Photo photo = new Photo();
            photo.setAlbumId(Long.valueOf(photoAddDto.getAlbumId()));
            photo.setUrl(url);
            photos.add(photo);
        }
        return saveOrUpdateBatch(photos);
    }
}




