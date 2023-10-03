package com.zdx.oss;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class FileTemplateImpl implements FileTemplate{
    @Autowired
    private Environment environment;
    @Override
    public String uploadFile(MultipartFile file, String bucketName) throws IOException {
        String path = environment.getProperty("HOME") + "/zdx/file/" + bucketName;
        if (!FileUtil.exist(path)) {
            FileUtil.mkdir(path);
        }
        IoUtil.copy(new ByteArrayInputStream(getCompression(file, bucketName)), new FileOutputStream(path + "/" + file.getOriginalFilename()));
        return "/zdx/file/" + bucketName + "/" +  file.getOriginalFilename();
    }

    @Override
    public Boolean checkFileIsExist(String bucketName, String fileName) {
        String path = environment.getProperty("HOME") + "/zdx/file/" + bucketName  + "/" + fileName;
        return FileUtil.isFile(path);
    }

    @Override
    public String getFileUrl(String bucketName, String fileName) {
        return "/zdx/file/" + bucketName + "/" +  fileName;
    }

    @Override
    public Boolean removeFile(String bucketName, String fileName) {
        String path = environment.getProperty("HOME") + "/zdx/file/" + bucketName  + "/" + fileName;
        return FileUtil.del(path);
    }

    @Override
    public void downloadFile(String bucketName, String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception {
        //设置编码
        response.setCharacterEncoding("UTF-8");
        //设置Content-Type头
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置Content-Disposition头 以附件形式解析
        String encodedFilename = getEncodedFilename(request, fileName);
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFilename);
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        String path = environment.getProperty("HOME") + "/zdx/file/" + bucketName  + "/" + fileName;
        FileInputStream fis = new FileInputStream(path);
        ServletOutputStream stream = response.getOutputStream();
        IoUtil.copy(fis, stream);
        fis.close();
    }

    @Override
    public InputStream getInputStream(String bucketName, String fileName) throws Exception {
        String path = environment.getProperty("HOME") + "/zdx/file/" + bucketName  + "/" + fileName;
        return new FileInputStream(path);
    }
}
