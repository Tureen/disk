/*
 * 描述：员工网盘信息业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.service;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.disk.EmployeeDisk;

public interface IEmployeeDiskService {

    /**
     * 添加网盘基本信息
     * @param employeeDisk
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int addEmployeeDisk(EmployeeDisk employeeDisk);
    
    /**
     * 修改网盘基本信息
     * @param employeeDisk
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int updateEmployeeDisk(EmployeeDisk employeeDisk);
    
    /**
     * 根据主键员工id查询网盘信息
     * @param id
     * @return  
     * EmployeeDisk 
     * @exception
     */
    EmployeeDisk getEmployeeDiskById(Integer id);
    
    /**
     * 获取最新各数据信息
     * @param employeeId
     * @return  
     * EmployeeDisk 
     * @exception
     */
    EmployeeDisk getNewEmployeeDisk(Integer employeeId);
}
