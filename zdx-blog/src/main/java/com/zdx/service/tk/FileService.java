package com.zdx.service.tk;

import com.zdx.entity.tk.File;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author zhaodengxuan
* @description 针对表【tk_file】的数据库操作Service
* @createDate 2023-07-05 17:32:37
*/
public interface FileService extends IService<File> {

    /**
     * 通过文件路径文件名称查询文件
     * @param type 文件路径
     * @param originalFilename 文件名
     * @return 文件
     */
    File getFileByTypeAndFileName(String type, String originalFilename);

    /**
     * 保存文件
     * @param file 前端上传文件
     * @param type 文件路径
     * @return 文件id,文件地址
     * @throws IOException
     */
    Map<String, String> saveFile(MultipartFile file, String type) throws IOException;

    /**
     * 批量删除文件
     * @param ids 文件id
     * @return
     */
    Boolean batchFileDelete(List<String> ids);

    void download(String id, HttpServletRequest request, HttpServletResponse response) throws Exception;

    String getFileUrl(String fileId);
}
