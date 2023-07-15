package com.zdx.service.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Album;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【zdx_album】的数据库操作Service
* @createDate 2023-07-14 17:23:34
*/
public interface AlbumService extends IService<Album> {

    /**
     * 分页查询相册
     * @param params 参数
     * @param queryWrapper 条件
     * @return 返回
     */
    IPage<Album> pageAlbum(RequestParams params, Wrapper<Album> queryWrapper);

    /**
     * 通过id查询相册
     * @param id id
     * @return 返回
     */
    Album getAlbumById(String id);
}
