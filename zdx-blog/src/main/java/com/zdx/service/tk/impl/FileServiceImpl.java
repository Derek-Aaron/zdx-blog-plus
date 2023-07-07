package com.zdx.service.tk.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.tk.File;
import com.zdx.service.tk.FileService;
import com.zdx.mapper.tk.FileMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【tk_file】的数据库操作Service实现
* @createDate 2023-07-05 17:32:37
*/
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
    implements FileService{

}




