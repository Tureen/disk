package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.authority.CpRole;
import com.yunip.model.authority.CpRolePermission;
import com.yunip.model.authority.query.CpRoleQuery;
import com.yunip.model.company.Employee;

/**
 * @author ming.zhu
 * 员工角色业务层
 */
public interface ICpRoleService {

    /**
     * 条件查询
     * @param query
     * @return  
     * EmployeeRoleQuery 
     * @exception
     */
    CpRoleQuery queryCpRole(CpRoleQuery query);
    
    /**
     * 根据id，获取角色
     * @param roleId
     * @return  
     * EmployeeRole 
     * @exception
     */
    CpRole getCpRole(Integer roleId);
    
    /**
     * 根据角色ID查询角色权限
     * @param roleid 角色ID
     * @return 
     * List<EmployeeRolePermission>
     */
    List<CpRolePermission> getCpRolePermission(Integer roleId);
    
    /**
     * 保存角色信息（新增/修改）
     * @param role 角色信息
     * boolean
     */
    @Transactional
    boolean saveOrUpdate(CpRole role);
    
    /**
     * 更改状态
     * @param roleid 
     * @param validStatus
     * void
     */
    @Transactional
    void updateValidStatus(Integer roleId,int validStatus);
    
    /**
     * 删除角色
     * @param roleId
     * void 
     * @exception
     */
    @Transactional
    int deleteRole(Integer roleId);
    
    /**
     * 添加角色与员工关系
     * @param cpRole  
     * void 
     * @exception
     */
    @Transactional
    void saveEmployeeRoles(int roleId,List<Employee> employees,List<Employee> allEmployees);
    
    /**
     * 获取角色对应员工id的拼接字符串
     * @param roleId
     * @return  
     * String 
     * @exception
     */
    String getEmployeeIdStrByRoleId(int roleId);
    
    /**
     * 根据员工id，返还员工权限id拼接字符串
     * @param employeeId
     * @return  
     * String 
     * @exception
     */
    String getPermissionIdStrByEmployeeId(int employeeId);
}
