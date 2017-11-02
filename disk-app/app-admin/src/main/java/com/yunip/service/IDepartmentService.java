package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Department;
import com.yunip.model.company.query.DepartmentQuery;

/**
 * @author ming.zhu
 * 部门service
 */
public interface IDepartmentService {
    
    /**
     * 添加部门
     * @param department 部门实体
     * @return
     */
    @Transactional
    Department addDepartment(Department department);
    
    /***
     * 更新部门信息
     * @param department 部门实体
     * @return
     */
    @Transactional
    int updateDepartment(Department department);
    
    /**
     * 获取所有部门列表
     * @return
     */
    List<Department> getAllDeparts();
    
    /**
     * 根据ID删除部门
     * @param deptId
     * @return
     */
    @Transactional
    int delDepartment(String deptId);
    
    /***
     * 根据部门ID 查询部门对象
     * @param deptId
     * @return
     */
    Department getDepartmentById(String deptId);
    
    /**
     * 根据条件获取部门列表
     * @param query
     * @return  
     * DepartmentQuery 
     * @exception
     */
    DepartmentQuery queryDepartment(DepartmentQuery query);
}
