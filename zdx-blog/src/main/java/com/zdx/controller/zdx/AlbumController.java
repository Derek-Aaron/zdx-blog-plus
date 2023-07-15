package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Album;
import com.zdx.handle.Result;
import com.zdx.service.zdx.AlbumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

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
        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Album::getAlbumName, params.getParam("name"));
        return queryWrapper;
    }

    @Override
    @GetMapping("/page")
    @ApiOperation("分页查询相册")
    public Result<IPage<Album>> page(RequestParams params) {
        return Result.success(albumService.pageAlbum(params, getQueryWrapper(params)));
    }


    @Override
    @GetMapping("/getById/{id}")
    @ApiOperation("根据id查询相册")
    public Result<Album> getById(@PathVariable @NotBlank String id) {
        return Result.success(albumService.getAlbumById(id));
    }
}
