package com.zdx.service.us.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.model.dto.AclDto;
import com.zdx.entity.us.Role;
import com.zdx.service.us.RoleService;
import com.zdx.mapper.us.RoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【us_role】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        return baseMapper.getRolesByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addOrDelResources(AclDto aclDto) {
        String type = aclDto.getType();
        if (type.equals("del")) {
            for (String subject : aclDto.getSubjects()) {
                baseMapper.delResources(subject, aclDto.getResources());
            }
        }
        if (type.equals("add")) {
            for (String subject : aclDto.getSubjects()) {
                baseMapper.addResources(subject, aclDto.getResources());
            }
        }
        return true;
    }
}




