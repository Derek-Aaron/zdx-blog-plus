package com.zdx.strategy.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.enums.ImportExportEnum;
import com.zdx.exception.BaseException;
import com.zdx.strategy.DataImportExportStrategy;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class JsonDataImportExportStrategy<T> implements DataImportExportStrategy<T> {
    @Override
    public String type() {
        return ImportExportEnum.JSON.name();
    }

    @Override
    public void importData(MultipartFile file, IService<T> iService) throws IOException {
        if (ObjUtil.isNull(file)) {
            throw new BaseException("");
        }
        String jsonStr = IoUtil.read(file.getInputStream(), StandardCharsets.UTF_8);
        JSONArray array = JSON.parseArray(jsonStr);
        List<T> entitys = new ArrayList<>();
        if (ObjUtil.isNotNull(array)) {
            for (int i = 0; i < array.size(); i++) {
                JSONObject json = array.getJSONObject(i);
                String id = json.getString("id");
                T entity = iService.getById(id);
                entity = (T) json.toJavaObject(entity.getClass());
                entitys.add(entity);
            }
            iService.saveOrUpdateBatch(entitys);
        }
    }

    @Override
    public void exportData(HttpServletResponse response, Wrapper<T> wrapper, IService<T> service) throws IOException {
        long count = service.count(wrapper);
        Map<Type, Type> typeMap = TypeUtil.getTypeMap(service.getClass());
        String entity = "";
        for (Map.Entry<Type, Type> entry : typeMap.entrySet()) {
            if (entry.getKey().getTypeName().equals("T")) {
                entity = entry.getValue().getTypeName();
            }
        }
        String fileName = "";
        try {
            Class<?> entityClazz = Class.forName(entity);
            TableName annotation = entityClazz.getAnnotation(TableName.class);
            fileName = annotation.value() + "." + type().toLowerCase(Locale.ROOT);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        long pageCount = ((count / 1000) + 1);
        List<T> entitys = new ArrayList<>();
        for (long i = 0; i < pageCount; i++) {
            IPage<T> page = new Page<>(pageCount, 1000);
            IPage<T> iPage = service.page(page, wrapper);
            entitys.addAll(iPage.getRecords());
        }
        String jsonStr = JSON.toJSONString(entitys);
        //设置编码
        response.setCharacterEncoding("UTF-8");
        //设置Content-Type头
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置Content-Disposition头 以附件形式解析
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        ByteArrayInputStream bis = new ByteArrayInputStream(jsonStr.getBytes(StandardCharsets.UTF_8));
        ServletOutputStream stream = response.getOutputStream();
        IoUtil.copy(bis, stream);
        bis.close();
    }
}
