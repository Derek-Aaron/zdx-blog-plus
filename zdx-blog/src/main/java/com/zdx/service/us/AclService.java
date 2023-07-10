package com.zdx.service.us;

import com.zdx.controller.dto.AclDto;
import com.zdx.entity.us.Acl;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【us_acl】的数据库操作Service
* @createDate 2023-07-05 17:30:41
*/
public interface AclService extends IService<Acl> {

    List<Acl> getAclsBySubject(List<String> ids);

    List<Acl> getSubjectByResource(String resource);

    Boolean saveAcl(AclDto aclDto);
}
