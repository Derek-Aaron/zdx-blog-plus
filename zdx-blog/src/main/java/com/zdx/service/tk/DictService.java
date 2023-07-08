package com.zdx.service.tk;

import com.zdx.entity.tk.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【tk_dict】的数据库操作Service
* @createDate 2023-07-07 21:00:30
*/
public interface DictService extends IService<Dict> {

    Dict getDictByKey(String key);


    Boolean saveDict(Dict dict);
}
