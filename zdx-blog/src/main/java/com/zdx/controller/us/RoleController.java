package com.zdx.controller.us;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.BaseController;
import com.zdx.controller.dto.AclDto;
import com.zdx.controller.dto.RequestParams;
import com.zdx.entity.us.Role;
import com.zdx.handle.Result;
import com.zdx.service.us.RoleService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;


@RestController
@RequestMapping("/zdx.role")
@Validated
@RequiredArgsConstructor
@Api(tags = "角色管理")
public class RoleController extends BaseController<Role> {

    private final RoleService roleService;
    @Override
    public IService<Role> getService() {
        return roleService;
    }

    @Override
    public Wrapper<Role> getQueryWrapper(RequestParams params) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(params.hasParam("name"), Role::getName, params.getParam("name"));
        queryWrapper.like(params.hasParam("display"), Role::getDisplay, params.getParam("display"));
        return queryWrapper;
    }

    @GetMapping("/roleIds/{userId}")
    public Result<List<String>> roleIds(@PathVariable @NotBlank String userId) {
        List<Role> roles = roleService.getRolesByUserId(Long.valueOf(userId));
        return Result.success(roles.stream().map(role -> String.valueOf(role.getId())).toList());
    }

    @PostMapping("/addOrDelResources")
    public Result<String> addOrDelResources(@RequestBody @Validated AclDto aclDto) {
        return roleService.addOrDelResources(aclDto) ? Result.success() : Result.error();
    }
}
