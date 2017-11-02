package com.yunip.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.user.Admin;

/**
 * @author ming.zhu
 * 前台员工的操作Service
 */
public interface IEmployeeService {
    /***
     * 添加员工
     * @param employee  员工对象
     * @return
     */
    @Transactional
    int addEmployee(Employee employee,Admin admin);
    
    /***
     * 修改员工
     * @param employee  员工对象
     * @return
     */
    @Transactional
    int updateEmployee(Employee employee,Admin admin);
    
    /***
     * 查询员工列表
     * @param query  查询对象
     * @return
     */
    EmployeeQuery getEmployeeByQuery(EmployeeQuery query);
    
    /***
     * 根据ID查询员工对象
     * @param employeeId 员工ID
     * @return
     */
    Employee getEmployeeById(Integer employeeId);
    
    /**
     * 查询员工对象,根据手机号或员工编号
     * @param employeeMobile
     * @param employeeCode
     * @return  
     * Employee 
     * @exception
     */
    Employee getEmployeeByCodeMobile(String employeeMobile,String employeeCode);
    
    /**
     * 检查员工工号是否存在,传入id判断修改号是否是自身
     * @param empCode
     * @param employeeId  
     * void 
     * @exception
     */
    void checkEmployeeCode(String empCode, Integer employeeId, Integer isAdmin);

    /**
     * 检查员工手机是否存在,传入id判断修改号是否是自身
     * @param mobile
     * @param employeeId  
     * void 
     * @exception
     */
    void checkEmployeeMobile(String mobile, Integer employeeId ,Integer isAdmin);
    
    /***
     * 更改状态
     * @param employee 员工对象
     */
    @Transactional
    int changeStatus(Employee employee);
    
    /***
     * 修改密码
     * @param employeeId 员工ID
     * @param initPwd    密码
     */
    @Transactional
    int initPwd(Integer employeeId,String initPwd);
    
    /**
     * 修改手机号
     * @param employeeId
     * @param mobile
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int changeMobile(Integer employeeId,String mobile);
    
    /**
     * 删除
     * @param employeeId
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int deleteEmployeeById(Integer employeeId);
    
    /***
     * 导入员工
     * @param employees
     * @param deptId
     * @param admin
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int importEmployees(List<Employee> employees, String deptId, Admin tdAdmin) throws Exception;
    
}
