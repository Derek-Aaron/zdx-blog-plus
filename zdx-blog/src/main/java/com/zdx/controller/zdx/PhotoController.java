package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Photo;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.PhotoAddDto;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.zdx.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "相册图片管理")
public class PhotoController {

    private final PhotoService photoService;
    @GetMapping("/zdx.photo/page")
    @Operation(summary = "后台分页查询相册图片")
    public Result<IPage<Photo>> adminPage(RequestParams params) {
        return Result.success(photoService.adminPage(params));
    }

    @PostMapping("/zdx.photo/add")
    @Operation(summary = "增加相册图片")
    @Log(type = LogEventEnum.SAVE, desc = "增加相册图片")
    @PreAuthorize("hasAnyAuthority('zdx:photo:add')")
    public Result<String> addPhoto(@RequestBody @Validated PhotoAddDto photoAddDto) {
        return photoService.addPhoto(photoAddDto) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.photo/save")
    @Operation(summary = "保存相册图片")
    @Log(type = LogEventEnum.SAVE, desc = "增加相册图片")
    @PreAuthorize("hasAnyAuthority('zdx:photo:save')")
    public Result<String> save(@RequestBody @Validated Photo photo) {
        return photoService.saveOrUpdate(photo) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.photo/batchDelete")
    @Operation(summary = "批量删除相册图片")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除相册图片")
    @PreAuthorize("hasAnyAuthority('zdx:photo:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "图片id") @NotEmpty List<String> ids) {
        return photoService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
