package com.zdx.controller.tk;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.Constants;
import com.zdx.annotation.Log;
import com.zdx.controller.BaseController;
import com.zdx.entity.tk.File;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.tk.FileService;
import com.zdx.utils.FileUtil;
import com.zdx.utils.MessageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/zdx.file")
@Validated
@RequiredArgsConstructor
@Tag(name = "文件管理")
public class FileController extends BaseController<File> {

    private final FileService fileService;

    @Override
    public IService<File> getService() {
        return fileService;
    }

    @Override
    public Wrapper<File> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), File::getName, params.getParam("name"));
        queryWrapper.like(params.hasParam("bucketName"), File::getBucketName, params.getParam("bucketName"));
        queryWrapper.orderByDesc(File::getCreateTime);
        return queryWrapper;
    }

    @GetMapping("/getUrl/{fileId}")
    @Operation(summary = "通过文件id获取访问地址")
    public Result<Map<String, String>> getUrl(@PathVariable @NotBlank String fileId) {
        return Result.success(Map.of("fileUrl", fileService.getFileUrl(fileId)));
    }


    @PostMapping("/upload")
    @Log(type = LogEventEnum.UPLOAD, desc = "上传文件")
    @Operation(summary = "上传文件")
    @PreAuthorize("hasAuthority('zdx:file:upload')")
    public Result<Object> upload(@Parameter(name = "file", description = "文件", required = true, schema = @Schema(type = "file")) MultipartFile[] file,
                                 @Parameter(name = "type", description = "文件类型", schema = @Schema(type = "type")) String type) throws IOException {
        List<Map<String, String>> list = new ArrayList<>();
        for (MultipartFile multipartFile : file) {
            if (multipartFile.getSize() > Constants.DOWNSIZE) {
                return Result.error(MessageUtil.message("zdx.down.size"));
            }
            if (StrUtil.isBlank(type)) {
                type = FileUtil.getFileType(multipartFile.getOriginalFilename());
            }
            Map<String, String> map = fileService.saveFile(multipartFile, type);
            list.add(map);
        }
        if (list.size() == 1) {
            return Result.success(list.get(0));
        }
        return Result.success(list);
    }

    @Override
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除文件")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除文件")
    @PreAuthorize("hasAuthority('zdx:upload:delete')")
    public Result<String> batchDelete(@RequestBody List<String> ids) {
        return fileService.batchFileDelete(ids) ? Result.success() : Result.error();
    }


    @GetMapping("/{id}/download")
    @Log(type = LogEventEnum.DOWNLOAD, desc = "下载文件")
    @Operation(summary = "文件下载")
    @PreAuthorize("hasAuthority('zdx:file:download')")
    public void download(@PathVariable @NotBlank String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileService.download(id, request, response);
    }
}
