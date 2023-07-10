package com.zdx.controller.us;


import com.zdx.annotation.Log;
import com.zdx.controller.dto.AclDto;
import com.zdx.entity.us.Acl;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.service.us.AclService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/zdx.acl")
@Validated
@RequiredArgsConstructor
@Api(tags = "权限")
public class AclController {

    private final AclService aclService;

    @GetMapping("/subject")
    @ApiOperation("通过资源获取权限")
    public Result<List<Acl>> subject(@NotNull @ApiParam("资源") String resource) {
        List<Acl> subjects = aclService.getSubjectByResource(resource);
        return Result.success(subjects);
    }

    @PostMapping("/save")
    @ApiOperation("保存权限")
    @Log(type = LogEventEnum.SAVE, desc = "保存权限")
    public Result<String> save(@RequestBody @Validated AclDto aclDto) {
        return aclService.saveAcl(aclDto) ? Result.success() : Result.error();
    }
}
