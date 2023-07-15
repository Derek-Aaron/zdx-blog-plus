package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.PhotoAddDpt;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Photo;
import com.zdx.handle.Result;
import com.zdx.service.zdx.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.photo")
@RequiredArgsConstructor
@Api(tags = "图片管理")
@Validated
public class PhotoController extends BaseController<Photo> {

    private final PhotoService photoService;
    @Override
    public IService<Photo> getService() {
        return photoService;
    }

    @Override
    public Wrapper<Photo> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Photo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Photo::getAlbumId, params.getParam("albumId"));
        return queryWrapper;
    }


    @PostMapping("/add")
    @ApiOperation("增加相册照片")
    public Result<String> add(@RequestBody @Validated PhotoAddDpt photoAddDpt) {
        return photoService.addPhoto(photoAddDpt) ? Result.success() : Result.error();
    }
}
