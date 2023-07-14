package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Album;
import com.zdx.service.zdx.AlbumService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.album")
@RequiredArgsConstructor
@Api(tags = "相册管理")
@Validated
public class AlbumController extends BaseController<Album> {

    private final AlbumService albumService;
    @Override
    public IService<Album> getService() {
        return albumService;
    }

    @Override
    public Wrapper<Album> getQueryWrapper(RequestParams params) {
        return new LambdaQueryWrapper<>();
    }
}
