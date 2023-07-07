package com.zdx.service.us.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.entity.us.Acl;
import com.zdx.service.us.AclService;
import com.zdx.mapper.us.AclMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【us_acl】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
public class AclServiceImpl extends ServiceImpl<AclMapper, Acl>
    implements AclService{

    @Override
    public List<Acl> getAclsBySubject(List<String> ids) {
        LambdaQueryWrapper<Acl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Acl::getSubject, ids);
        return list(queryWrapper);
    }
}




