package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.Role;
import com.yunip.model.authority.query.RoleQuery;

/**
 * @author ming.zhu
 * 角色Dao
 */
@Repository("iRoleDao")
public interface IRoleDao extends IBaseDao<Role>{
    
    /**
     * selectCountByQuery(条件查询，查询结果数量) 
     * @param query
     * @return  
     * int 
     * @exception
     */
    int selectCountByQuery(RoleQuery query);
    
    /**
     * selectByQuery(条件查询结果列表) 
     * @param query
     * @return  
     * List<Role> 
     * @exception
     */
    List<Role> selectByQuery(RoleQuery query);
    
    /**
     * delById(删除，通过id与systemCode) 
     * @param role
     * @return  
     * int 
     * @exception
     */
    int delById(Role role);
}
