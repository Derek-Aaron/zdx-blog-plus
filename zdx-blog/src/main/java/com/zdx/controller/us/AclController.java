package com.zdx.controller.us;


import com.zdx.annotation.Log;
import com.zdx.entity.us.Acl;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.AclDto;
import com.zdx.service.us.AclService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/zdx.acl")
@Validated
@RequiredArgsConstructor
@Tag(name = "权限")
public class AclController {

    private final AclService aclService;

    @GetMapping("/subject")
    @Operation(summary = "通过资源获取权限")
    public Result<List<Acl>> subject(@NotNull @Parameter(name = "resource", description = "资源") String resource) {
        List<Acl> subjects = aclService.getSubjectByResource(resource);
        return Result.success(subjects);
    }

    @PostMapping("/save")
    @Operation(summary = "保存权限")
    @Log(type = LogEventEnum.SAVE, desc = "保存权限")
    @PreAuthorize("hasAnyAuthority('zdx:user:auth', 'zdx:menu:auth')")
    public Result<String> save(@RequestBody @Validated AclDto aclDto) {
        return aclService.saveAcl(aclDto) ? Result.success() : Result.error();
    }
}
