package com.zdx.service.us.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Account;
import com.zdx.service.us.AccountService;
import com.zdx.mapper.us.AccountMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author zhaodengxuan
* @description 针对表【us_account】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAccount(Account data) {
        Account account = getById(data.getId());
        if (ObjUtil.isNotNull(account)) {
            data.setPassword(account.getPassword());
        }
        return saveOrUpdate(data);
    }
}




