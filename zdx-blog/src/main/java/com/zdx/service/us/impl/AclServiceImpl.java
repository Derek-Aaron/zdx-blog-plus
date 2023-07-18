package com.zdx.service.us.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdx.Constants;
import com.zdx.model.dto.AclDto;
import com.zdx.entity.tk.Menu;
import com.zdx.entity.us.Acl;
import com.zdx.mapper.us.AclMapper;
import com.zdx.service.tk.MenuService;
import com.zdx.service.us.AclService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
* @author zhaodengxuan
* @description 针对表【us_acl】的数据库操作Service实现
* @createDate 2023-07-05 17:30:41
*/
@Service
@RequiredArgsConstructor
public class AclServiceImpl extends ServiceImpl<AclMapper, Acl>
    implements AclService{

    private final MenuService menuService;

    @Override
    public List<Acl> getAclsBySubject(List<String> ids) {
        LambdaQueryWrapper<Acl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Acl::getSubject, ids);
        return list(queryWrapper);
    }

    @Override
    public List<Acl> getSubjectByResource(String resource) {
        LambdaQueryWrapper<Acl> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Acl::getResource, resource);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveAcl(AclDto aclDto) {
        List<Acl> acls = new ArrayList<>();
        if (aclDto.getType().equals("add")) {
            for (String subject : aclDto.getSubjects()) {
                for (String resource : aclDto.getResources()) {
                    LambdaQueryWrapper<Acl> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(Acl::getSubject, subject);
                    queryWrapper.eq(Acl::getResource, resource);
                    Acl acl = getOne(queryWrapper);
                    if (ObjUtil.isNull(acl)) {
                        acl = new Acl();
                        acl.setSubject(subject);
                        acl.setResource(resource);
                        if (resource.startsWith(Constants.ACL_MENU_PREFIX)) {
                            LambdaQueryWrapper<Menu> menuQueryWrapper = new LambdaQueryWrapper<>();
                            menuQueryWrapper.select(Menu::getParams);
                            menuQueryWrapper.eq(Menu::getId, resource.replaceAll(Constants.ACL_MENU_PREFIX, ""));
                            Menu menu = menuService.getOne(menuQueryWrapper);
                            if (ObjUtil.isNotNull(menu)) {
                                acl.setParams(menu.getParams());
                            }
                        }
                        acls.add(acl);
                    }
                }
            }
            return saveOrUpdateBatch(acls);
        }
        if (aclDto.getType().equals("del")) {
            List<Long> aclIds = new ArrayList<>();
            for (String subject : aclDto.getSubjects()) {
                for (String resource : aclDto.getResources()) {
                    LambdaQueryWrapper<Acl> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.select(Acl::getId);
                    queryWrapper.eq(Acl::getSubject, subject);
                    queryWrapper.eq(Acl::getResource, resource);
                    Acl acl = getOne(queryWrapper);
                    if (ObjUtil.isNotNull(acl)) {
                        aclIds.add(acl.getId());
                    }
                }
            }
            return removeBatchByIds(aclIds);
        }
        return false;
    }
}




