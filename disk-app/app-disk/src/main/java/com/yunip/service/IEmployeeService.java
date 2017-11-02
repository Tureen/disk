/*
 * 描述：员工业务层
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.disk.Contact;
import com.yunip.model.user.Admin;

/**
 * @author ming.zhu
 * 前台员工的操作Service
 */
public interface IEmployeeService {
    
    void saveOrUpdate(Admin admin,String employeeName);
    
    /***
     * 添加员工
     * @param employee  员工对象
     * @return
     */
    @Transactional
    int addEmployee(Employee employee,Admin admin);
   
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
    
    /***
     * 获取全部的员工
     * @return  
     * Map<String, List<Employee>> 
     * @exception
     */
    Map<String, List<Employee>> getAllEmployees();
    
    /***
     * 获取全部的员工
     * @return  
     * Map<String, List<Contact>> 
     * @exception
     */
    Map<String, List<Contact>> getAllContactEmployees(Integer employeeId);
    
    /**
     * 根据员工id修改对象
     * @param employee
     * @return  
     * int 
     * @exception
     */
    @Transactional
    int updateEmployeeById(Employee employee);
    
    /**
     * 检查员工工号是否存在,传入id判断修改号是否是自身
     * @param empCode
     * @param employeeId  
     * void 
     * @exception
     */
    void checkEmployeeCode(String empCode, Integer employeeId);

    /**
     * 检查员工手机是否存在,传入id判断修改号是否是自身
     * @param mobile
     * @param employeeId  
     * void 
     * @exception
     */
    void checkEmployeeMobile(String mobile, Integer employeeId);
    
    /**
     * service简单调用dao
     * @param employeeQuery
     * @return  
     * Employee 
     * @exception
     */
    Employee selectEmpByQuery(EmployeeQuery employeeQuery);
    
    /**
     * 注册员工人数限制
     * checkRegisterLimit(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param type  
     * void 
     * @exception
     */
    void checkRegisterLimit(int type,Integer intoNum);
    
    /**
     * 根据token获取员工基本数据
     * @param token 登录表示
     * @return
     */
    Employee getEmployee(String token,String ip);
}
