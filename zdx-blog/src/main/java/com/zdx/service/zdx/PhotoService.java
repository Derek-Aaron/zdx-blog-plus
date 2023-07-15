package com.zdx.service.zdx;

import com.zdx.controller.dto.PhotoAddDpt;
import com.zdx.entity.zdx.Photo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【zdx_photo】的数据库操作Service
* @createDate 2023-07-14 17:23:35
*/
public interface PhotoService extends IService<Photo> {

    /**
     * 通过相册id获取照片个数
     * @param albumId 相册id
     * @return 返回
     */
    Long getPhotoCountByAlumId(Long albumId);

    /**
     * 新增相册图片
     * @param photoAddDpt 增加相册图片
     * @return 返回
     */
    boolean addPhoto(PhotoAddDpt photoAddDpt);

}
