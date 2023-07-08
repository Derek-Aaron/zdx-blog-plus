package com.zdx.service.us;

import com.zdx.controller.dto.AclDto;
import com.zdx.entity.us.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【us_role】的数据库操作Service
* @createDate 2023-07-05 17:30:41
*/
public interface RoleService extends IService<Role> {

    /**
     * 根据用户id查询用户
     * @param userId 用户id
     * @return 角色
     */
    List<Role> getRolesByUserId(Long userId);

    Boolean addOrDelResources(AclDto aclDto);
}
