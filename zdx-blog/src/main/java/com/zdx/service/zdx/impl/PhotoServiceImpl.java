package com.zdx.service.zdx.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.controller.dto.PhotoAddDpt;
import com.zdx.entity.zdx.Photo;
import com.zdx.service.zdx.PhotoService;
import com.zdx.mapper.zdx.PhotoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【zdx_photo】的数据库操作Service实现
* @createDate 2023-07-14 17:23:35
*/
@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo>
    implements PhotoService{

    @Override
    public Long getPhotoCountByAlumId(Long albumId) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, albumId);
        return count(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addPhoto(PhotoAddDpt photoAddDpt) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, photoAddDpt.getAlbumId());
        List<Photo> photos = new ArrayList<>();
        long count = count(queryWrapper);
        if (count == 0 || remove(queryWrapper)) {
            for (String url : photoAddDpt.getPhotoUrlList()) {
                Photo photo = new Photo();
                photo.setAlbumId(Long.valueOf(photoAddDpt.getAlbumId()));
                photo.setUrl(url);
                photos.add(photo);
            }
        }
        return saveOrUpdateBatch(photos);
    }
}




