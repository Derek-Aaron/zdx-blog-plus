package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Photo;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.PhotoAddDto;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.PhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Api(tags = "相册图片管理")
public class PhotoController {

    private final PhotoService photoService;
    @GetMapping("/zdx.photo/page")
    @ApiOperation("后台分页查询相册图片")
    public Result<IPage<Photo>> adminPage(RequestParams params) {
        return Result.success(photoService.adminPage(params));
    }

    @PostMapping("/zdx.photo/add")
    @ApiOperation("增加相册图片")
    @Log(type = LogEventEnum.SAVE, desc = "增加相册图片")
    public Result<String> addPhoto(@RequestBody @Validated PhotoAddDto photoAddDto) {
        return photoService.addPhoto(photoAddDto) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.photo/save")
    @ApiOperation("保存相册图片")
    @Log(type = LogEventEnum.SAVE, desc = "增加相册图片")
    public Result<String> save(@RequestBody @Validated Photo photo) {
        return photoService.saveOrUpdate(photo) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.photo/batchDelete")
    @ApiOperation("批量删除相册图片")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除相册图片")
    public Result<String> batchDelete(@RequestBody @ApiParam("图片id") @NotEmpty List<String> ids) {
        return photoService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
