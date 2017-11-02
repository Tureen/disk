/**
 * 
 */
package com.yunip.service;

import com.yunip.model.company.Employee;

/**
 * @author can.du
 * 员工服务
 */
public interface IEmployeeService {
    /**
     * 根据token获取员工基本数据
     * @param token 登录表示
     * @return
     */
    Employee getEmployee(String token) throws Exception;
    
    /**
     * 根据员工ID获取员工基本数据
     * @param employeeId 员工ID
     */
    Employee getEmployeeByEmployeeId(String employeeId) throws Exception;
}

