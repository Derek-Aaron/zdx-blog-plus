package com.zdx.oss;


import cn.hutool.core.io.IoUtil;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;


@Slf4j
public class MinioTemplateImpl implements FileTemplate{
    @Autowired
    private MinioClient minioClient;
    @Override
    public String uploadFile(MultipartFile file, String bucketName) {
        try {
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName.toLowerCase(Locale.ROOT)).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName.toLowerCase(Locale.ROOT))
                        .build());
            }
            byte[] bytes = getCompression(file, bucketName);
            minioClient.putObject(PutObjectArgs.builder().object(file.getOriginalFilename())
                    .bucket(bucketName.toLowerCase(Locale.ROOT))
                    .contentType(file.getContentType())
                    .stream(new ByteArrayInputStream(bytes), bytes.length, -1).build());
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName.toLowerCase(Locale.ROOT))
                            .object(file.getOriginalFilename())
                            .build()
            );
        } catch (Exception e) {
            log.error("上传文件出现异常：{}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Boolean checkFileIsExist(String bucketName, String fileName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName.toLowerCase(Locale.ROOT)).object(fileName).build()
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public String getFileUrl(String bucketName, String fileName) {
        if (checkFileIsExist(bucketName, fileName)) {
            try {
                return minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(bucketName.toLowerCase(Locale.ROOT))
                                .object(fileName)
                                .build()
                );
            } catch (Exception e) {
                log.error("获取文件访问连接异常：{}", e.getMessage(), e);
                return null;
            }
        }
        return null;
    }

    @Override
    public Boolean removeFile(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName.toLowerCase(Locale.ROOT))
                    .object(fileName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("删除文件出现异常：{}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void downloadFile(String bucketName, String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception{
//设置编码
        response.setCharacterEncoding("UTF-8");
        //设置Content-Type头
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置Content-Disposition头 以附件形式解析
        String encodedFilename = getEncodedFilename(request, fileName);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFilename);
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        InputStream is = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName.toLowerCase(Locale.ROOT))
                .object(fileName)
                .build());
        ServletOutputStream stream = response.getOutputStream();
        IoUtil.copy(is, stream);
        is.close();
    }
}
