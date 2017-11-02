package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.CpPermission;
import com.yunip.model.authority.query.CpPermissionQuery;

/**
 * @author ming.zhu
 * 员工权限Dao
 */
@Repository("iCpPermissionDao")
public interface ICpPermissionDao extends IBaseDao<CpPermission>{

    /**
     * 
     * selectCountByQuery(条件查询，查询结果数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(CpPermissionQuery query);
    
    /**
     * 
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<EmployeePermission> 
     * @exception
     */
    List<CpPermission> selectByQuery(CpPermissionQuery query);
}
