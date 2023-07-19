package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.entity.zdx.Album;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.AlbumPhotoCountVo;
import com.zdx.model.vo.front.PhotoInfoVo;

/**
* @author zhaodengxuan
* @description 针对表【zdx_album】的数据库操作Service
* @createDate 2023-07-17 16:51:40
*/
public interface AlbumService extends IService<Album> {

    /**
     * 分页查询相册
     * @param params 查询参数
     * @return 返回
     */
    IPage<AlbumPhotoCountVo> adminPage(RequestParams params);

    /**
     * 通过id查询相册
     * @param id 相册id
     * @return 返回
     */
    AlbumPhotoCountVo getAlbumPhotoCountById(String id);

    /**
     * 通过相册id获取图片
     * @param albumId 相册id
     * @return 返回
     */
    PhotoInfoVo homePhotoList(String albumId);
}
