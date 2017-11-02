package com.yunip.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yunip.enums.common.ValidStatus;
import com.yunip.mapper.authority.ICpPermissionDao;
import com.yunip.mapper.authority.ICpRoleDao;
import com.yunip.mapper.authority.ICpRolePermissionDao;
import com.yunip.model.authority.CpPermission;
import com.yunip.model.authority.query.CpPermissionQuery;
import com.yunip.service.ICpPermissionService;

/**
 * @author ming.zhu
 * 员工权限业务层
 */
@Service("iCpPermissionService")
public class CpPermissionServiceImpl implements ICpPermissionService{

    @Resource(name = "iCpPermissionDao")
    private ICpPermissionDao cpPermissionDao;
    
    @Resource(name = "iCpRoleDao")
    private ICpRoleDao cpRoleDao;
    
    @Resource(name = "iCpRolePermissionDao")
    private ICpRolePermissionDao cpRolePermissionDao;

    @Override
    public List<CpPermission> getPermissions() {
        CpPermissionQuery query = new CpPermissionQuery();
        query.setValidStatus(ValidStatus.NOMAL.getStatus());
        query.setPageSize(Integer.MAX_VALUE);
        List<CpPermission> list = cpPermissionDao.selectByQuery(query);
        return list;
    }
}
