package com.zdx.service.tk.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.tk.File;
import com.zdx.handle.Result;
import com.zdx.mapper.tk.FileMapper;
import com.zdx.oss.FileTemplate;
import com.zdx.service.tk.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author zhaodengxuan
* @description 针对表【tk_file】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
@RequiredArgsConstructor
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService{

    private final FileTemplate fileTemplate;


    @Override
    public File getFileByTypeAndFileName(String type, String originalFilename) {
        LambdaQueryWrapper<File> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(File::getBucketName, type);
        queryWrapper.eq(File::getName, originalFilename);
        return getOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> saveFile(MultipartFile file, String type) throws IOException {
        Map<String, String> result = new HashMap<>();
        File fileDb = getFileByTypeAndFileName(type, file.getOriginalFilename());
        if (ObjUtil.isNotNull(fileDb)) {
            String fileUrl = fileTemplate.getFileUrl(fileDb.getBucketName(), file.getOriginalFilename());
            result.put("fileId", String.valueOf(fileDb.getId()));
            result.put("fileUrl", fileUrl);
            return result;
        }
        fileDb = new File();
        fileDb.setBucketName(type);
        fileDb.setName(file.getOriginalFilename());
        fileDb.setSize(file.getSize());
        fileDb.setMd5(MD5.create().digestHex(file.getInputStream()));
        if (saveOrUpdate(fileDb)) {
            String uploadFile = fileTemplate.uploadFile(file, type);
            result.put("fileId", String.valueOf(fileDb.getId()));
            result.put("fileUrl", uploadFile);
            return result;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchFileDelete(List<String> ids) {
        for (String id : ids) {
            File file = getById(id);
            if (ObjUtil.isNotNull(file)) {
                fileTemplate.removeFile(file.getBucketName(), file.getName());
            }
        }
        return removeBatchByIds(ids);
    }

    @Override
    public void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = getById(id);
        if (ObjUtil.isNull(file)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getOutputStream().write(JSON.toJSONBytes(Result.error("zdx.file.null")));
            return;
        }
        fileTemplate.downloadFile(file.getBucketName(), file.getName(), response, request);
    }

    @Override
    public String getFileUrl(String fileId) {
        File file = getById(fileId);
        if (ObjUtil.isNotNull(file)) {
            return fileTemplate.getFileUrl(file.getBucketName(), file.getName());
        }
        return null;
    }
}




