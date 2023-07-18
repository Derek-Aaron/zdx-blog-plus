package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.entity.zdx.Photo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.PhotoAddDto;
import com.zdx.model.dto.RequestParams;

/**
* @author zhaodengxuan
* @description 针对表【zdx_photo】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface PhotoService extends IService<Photo> {

    /**
     * 分页查询相册图片
     * @param params 请求参数
     * @return 返回
     */
    IPage<Photo> adminPage(RequestParams params);

    /**
     * 新增相册图片
     * @param photoAddDto 实体
     * @return 返回
     */
    boolean addPhoto(PhotoAddDto photoAddDto);
}
