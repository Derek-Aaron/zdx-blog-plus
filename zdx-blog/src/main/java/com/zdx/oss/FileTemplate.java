package com.zdx.oss;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import com.zdx.enums.FileTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public interface FileTemplate {

    /**
     * 上传文件
     * @param file
     * @param bucketName
     * @return
     */
    String uploadFile(MultipartFile file, String bucketName) throws IOException;


    /**
     * 文件是否存在
     * @param bucketName
     * @param fileName
     * @return
     */
    Boolean checkFileIsExist(String bucketName, String fileName);

    /**
     * 获取文件访问路径
     * @param bucketName
     * @param fileName
     * @return
     */
    String getFileUrl(String bucketName, String fileName);

    /**
     * 删除文件
     * @param bucketName
     * @param fileName
     * @return
     */
    Boolean removeFile(String bucketName, String fileName);

    /**
     * 下载文件
     * @param bucketName
     * @param fileName
     * @param response
     * @param request
     */
    void downloadFile(String bucketName, String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception;

    default String getEncodedFilename(HttpServletRequest request, String fileName) {
        String encodedFilename = null;
        String agent = request.getHeader("User-Agent");
        if (agent.contains("MSIE")) {
            //IE浏览器
            encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            encodedFilename = encodedFilename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            //火狐浏览器
            Base64.Encoder encoder = Base64.getEncoder();
            encodedFilename = "=?utf-8?B?" + encoder.encodeToString(fileName.getBytes(StandardCharsets.UTF_8)) + "?=";
        } else {
            //其他浏览器
            encodedFilename = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        }
        return encodedFilename;
    }

    default byte[] getCompression(MultipartFile file, String bucketName) throws IOException {
        ByteArrayOutputStream bos = null;
        if (FileTypeEnum.IMAGE.name().equals(bucketName) && file.getSize() >= 1024 * 1024) {
            bos = new ByteArrayOutputStream();
            ImgUtil.scale(file.getInputStream(), bos, 0.5F);
        } else {
            bos = new ByteArrayOutputStream();
            FastByteArrayOutputStream stream = IoUtil.read(file.getInputStream());
            bos.writeBytes(stream.toByteArray());
        }
        return bos.toByteArray();
    }
}
