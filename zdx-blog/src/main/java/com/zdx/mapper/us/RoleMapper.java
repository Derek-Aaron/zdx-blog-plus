package com.zdx.mapper.us;

import com.zdx.entity.us.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【us_role】的数据库操作Mapper
* @createDate 2023-07-05 17:30:41
* @Entity com.zdx.entity.us.Role
*/
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色
     * @param userId 用户id
     * @return 角色
     */
    List<Role> getRolesByUserId(@Param("userId") Long userId);

    /**
     * 删除角色
     * @param subject 主体
     * @param resources 资源
     * @return 返回
     */
    Boolean delResources(@Param("subject") String subject, @Param("resources") List<String> resources);


    /**
     * 添加角色
     * @param subject 主体
     * @param resources 资源
     * @return  返回
     */
    Boolean addResources(@Param("subject") String subject, @Param("resources") List<String> resources);
}




