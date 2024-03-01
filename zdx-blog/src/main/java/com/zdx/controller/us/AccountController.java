package com.zdx.controller.us;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.annotation.Encrypt;
import com.zdx.annotation.Log;
import com.zdx.controller.BaseController;
import com.zdx.entity.us.Account;
import com.zdx.enums.LogEventEnum;
import com.zdx.handle.Result;
import com.zdx.model.dto.RequestParams;
import com.zdx.service.us.AccountService;
import com.zdx.utils.MybatisPlusUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/zdx.account")
@Validated
@RequiredArgsConstructor
@Tag(name = "账号管理")
public class AccountController extends BaseController<Account> {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;
    @Override
    public IService<Account> getService() {
        return accountService;
    }

    @Override
    public Wrapper<Account> getQueryWrapper(RequestParams params) {
        String userId = params.getParam("userId", String.class);
        return new LambdaQueryWrapper<Account>().eq(Account::getUserId, userId);
    }

    @Override
    @GetMapping("/page")
    @Operation(summary = "分页查询账号")
    public Result<IPage<Account>> page(RequestParams params) {
        Result<IPage<Account>> page = super.page(params);
        List<Account> records = page.getData().getRecords();
        List<Account> accounts = new ArrayList<>();
        for (Account record : records) {
            record.setPassword(null);
            accounts.add(record);
        }
        return Result.success(MybatisPlusUtils.pageConvert(page.getData(), accounts));
    }

    @PostMapping("/save")
    @Log(type = LogEventEnum.SAVE, desc = "保存用户账号")
    @PreAuthorize("hasAnyAuthority('zdx:account:save','zdx:account:add')")
    @Encrypt
    @Operation(summary = "保存用户账号")
    public Result<String> save(@RequestBody @Validated @Encrypt Account data) {
        if (StrUtil.isNotBlank(data.getPassword())) {
            data.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        return accountService.saveAccount(data) ? Result.success() : Result.error();
    }
}
