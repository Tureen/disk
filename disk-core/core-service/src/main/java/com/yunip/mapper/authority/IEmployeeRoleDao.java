package com.yunip.mapper.authority;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yunip.mapper.base.IBaseDao;
import com.yunip.model.authority.EmployeeRole;

/**
 * @author ming.zhu
 * 用户与角色关系
 */
@Repository("iEmployeeRoleDao")
public interface IEmployeeRoleDao extends IBaseDao<EmployeeRole>{
    /**
     * selectByRoleId(条件查询,根据roleId) 
     * @param roleId
     * @return  
     * List<EmployeeRole> 
     * @exception
     */
    List<EmployeeRole> selectByRoleId(Integer roleId);
    
    /**
     * selectByEmployeeId(条件查询,根据adminId) 
     * @param adminId
     * @return  
     * List<EmployeeRole> 
     * @exception
     */
    List<EmployeeRole> selectByEmployeeId(Integer employeeId);
    
    /**
     * delByEmployeeId(删除，通过employeeId) 
     * @param employeeId
     * @return  
     * int 
     * @exception
     */
    int delByEmployeeId(Integer employeeId);
    
    /**
     * delByRoleId(删除，通过roleId) 
     * @param roleId
     * @return  
     * int 
     * @exception
     */
    int delByRoleId(Integer roleId);
    
    /**
     * delByPrimaryKey(删除，根据主键) 
     * @param employeeRole
     * @return  
     * @exception
     */
    int delByPrimaryKey(EmployeeRole employeeRole);
}
