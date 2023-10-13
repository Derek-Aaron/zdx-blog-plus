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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/zdx.file")
@Validated
@RequiredArgsConstructor
@Api(tags = "文件管理")
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
        return queryWrapper;
    }

    @GetMapping("/getUrl/{fileId}")
    @ApiOperation("通过文件id获取访问地址")
    public Result<Map<String, String>> getUrl(@PathVariable @NotBlank String fileId) {
        return Result.success(Map.of("fileUrl", fileService.getFileUrl(fileId)));
    }


    @PostMapping("/upload")
    @Log(type = LogEventEnum.UPLOAD, desc = "上传文件")
    @ApiOperation("上传文件")
    @PreAuthorize("hasAuthority('zdx:file:upload')")
    public Result<Object> upload(@ApiParam("文件") MultipartFile[] file, @ApiParam("文件类型") String type) throws IOException {
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
    @ApiOperation("批量删除文件")
    @Log(type = LogEventEnum.DELETE, desc = "批量删除文件")
    @PreAuthorize("hasAuthority('zdx:upload:delete')")
    public Result<String> batchDelete(@RequestBody List<String> ids) {
        return fileService.batchFileDelete(ids) ? Result.success() : Result.error();
    }


    @GetMapping("/{id}/download")
    @Log(type = LogEventEnum.DOWNLOAD, desc = "下载文件")
    @ApiOperation("文件下载")
    @PreAuthorize("hasAuthority('zdx:file:download')")
    public void download(@PathVariable @NotBlank String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        fileService.download(id, request, response);
    }
}
