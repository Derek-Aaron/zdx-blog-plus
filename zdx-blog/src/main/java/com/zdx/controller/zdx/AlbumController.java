package com.zdx.controller.zdx;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zdx.annotation.Log;
import com.zdx.entity.zdx.Album;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.model.vo.AlbumPhotoCountVo;
import com.zdx.model.vo.front.PhotoInfoVo;
import com.zdx.service.zdx.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Tag(name = "相册管理")
public class AlbumController {

    private final AlbumService albumService;


    @GetMapping("/home/zdx.album/list")
    @Operation(summary = "前台查询全部相册")
    public Result<List<Album>> homeList() {
        LambdaQueryWrapper<Album> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Album::getStatus, Boolean.TRUE);
        queryWrapper.orderByDesc(Album::getCreateTime);
        return Result.success(albumService.list(queryWrapper));
    }

    @GetMapping("/home/zdx.album/photoList/{albumId}")
    @Operation(summary = "前台相册id查询图片列表")
    public Result<PhotoInfoVo> homePhotoList(@PathVariable @NotBlank String albumId) {
        return Result.success(albumService.homePhotoList(albumId));
    }
    @GetMapping("/zdx.album/page")
    @Operation(summary = "后台分页查询相册")
    public Result<IPage<AlbumPhotoCountVo>> adminPage(RequestParams params) {
        return Result.success(albumService.adminPage(params));
    }

    @GetMapping("/zdx.album/getById/{id}")
    @Operation(summary = "通过相册id查询相册图片列表")
    public Result<AlbumPhotoCountVo> getAlbumPhotoCountById(@PathVariable @NotBlank String id) {
        return Result.success(albumService.getAlbumPhotoCountById(id));
    }

    @PostMapping("/zdx.album/save")
    @Operation(summary = "保存相册")
    @Log(type = LogEventEnum.SAVE, desc = "保存相册")
    @PreAuthorize("hasAnyAuthority('zdx:album:add', 'zdx:album:save')")
    public Result<String> save(@RequestBody @Validated Album album) {
        return albumService.saveOrUpdate(album) ? Result.success() : Result.error();
    }

    @PostMapping("/zdx.album/batchDelete")
    @Operation(summary = "批量删除相册")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除相册")
    @PreAuthorize("hasAuthority('zdx:album:delete')")
    public Result<String> batchDelete(@RequestBody @Parameter(description = "标签id") @NotEmpty List<String> ids) {
        return albumService.removeBatchByIds(ids) ? Result.success() : Result.error();
    }
}
