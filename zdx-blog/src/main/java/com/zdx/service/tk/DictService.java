package com.zdx.service.tk;

import com.zdx.entity.tk.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zhaodengxuan
* @description 针对表【tk_dict】的数据库操作Service
* @createDate 2023-07-07 21:00:30
*/
public interface DictService extends IService<Dict> {

    /**
     * 通过key获取字典（会缓存）
     * @param key key
     * @return 字典
     */
    Object getDictByKey(String key);


    /**
     * 保存字典数据（会清除相应key的缓存）
     * @param dict 字典
     * @return 成功
     */
    Boolean saveDict(Dict dict);
}
