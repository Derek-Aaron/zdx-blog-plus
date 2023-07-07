package com.zdx.service.us.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Account;
import com.zdx.service.us.AccountService;
import com.zdx.mapper.us.AccountMapper;
import org.springframework.stereotype.Service;

/**
* @author zhaodengxuan
* @description 针对表【us_account】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account>
    implements AccountService{

}




