package com.zdx.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public class MybatisPlusUtils {

    public static  <T> IPage<T> pageConvert(IPage<?> page, List<T> list) {
        IPage<T> iPage = new Page<>();
        iPage.setPages(page.getPages());
        iPage.setTotal(page.getTotal());
        iPage.setSize(page.getSize());
        iPage.setCurrent(page.getCurrent());
        iPage.setRecords(list);
        return iPage;
    }
}
