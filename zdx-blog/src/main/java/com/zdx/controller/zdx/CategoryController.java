package com.zdx.controller.zdx;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.zdx.Category;
import com.zdx.service.zdx.CategoryService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zdx.category")
@RequiredArgsConstructor
@Api(tags = "分类管理")
@Validated
public class CategoryController extends BaseController<Category> {

    private final CategoryService categoryService;
    @Override
    public IService<Category> getService() {
        return categoryService;
    }

    @Override
    public Wrapper<Category> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Category::getName, params.getParam("name"));
        return queryWrapper;
    }
}
