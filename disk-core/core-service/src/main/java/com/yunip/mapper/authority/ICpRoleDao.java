package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.CpRole;
import com.yunip.model.authority.query.CpRoleQuery;

/**
 * @author ming.zhu
 * 员工角色Dao
 */
@Repository("iCpRoleDao")
public interface ICpRoleDao extends IBaseDao<CpRole>{

    /**
     * selectCountByQuery(条件查询，查询结果数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(CpRoleQuery query);
    
    /**
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<EmployeeRole> 
     * @exception
     */
    List<CpRole> selectByQuery(CpRoleQuery query);
}
