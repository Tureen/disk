/*
 * 描述：部门业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.service;

import java.util.List;

import com.yunip.model.company.Department;

/**
 * @author ming.zhu
 * 部门service
 */
public interface IDepartmentService {
    
    /**
     * 获取所有部门列表
     * @return
     */
    List<Department> getAllDeparts();
    
    /***
     * 根据部门ID 获取部门对象
     * @param deptId
     * @return  
     * Department 
     * @exception
     */
    Department getDepartmentById(String deptId);
}
