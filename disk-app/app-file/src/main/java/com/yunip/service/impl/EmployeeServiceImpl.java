package com.yunip.service.impl;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunip.mapper.company.IDepartmentDao;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.user.IAdminDao;
import com.yunip.model.company.Employee;
import com.yunip.model.user.Admin;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IEmployeeService;
import com.yunip.service.user.IAdminService;

/**
 * @author can.du
 * 员工service的实现类
 */
@Service("iEmployeeService")
public class EmployeeServiceImpl implements IEmployeeService {

    @Resource(name = "iEmployeeDao")
    private IEmployeeDao    employeeDao;

    @Resource(name = "iDepartmentDao")
    private IDepartmentDao  departmentDao;
    
    @Resource(name = "iAdminService")
    private IAdminService adminService;
    
    @Resource(name = "iAdminDao")
    private IAdminDao adminDao;

    @Override
    public Employee getEmployee(String token) throws Exception {
        AdminQuery query = new AdminQuery();
        if(!StringUtils.isNotBlank(token)){
            throw new Exception("登录失效");
        }
        query.setToken(token);
        
        //先查询登录信息
        Admin admin = adminService.getLoginAdmin(query);
        if(admin == null ){
            throw new Exception("登录失效");
        }
        Employee employee = employeeDao.selectById(admin.getId());
        if (employee == null) {
            throw new Exception("登录失效");
        }
        return employee;
    }

    @Override
    public Employee getEmployeeByEmployeeId(String employeeId) throws Exception {
        Employee employee = employeeDao.selectById(employeeId);
        if (employee == null) {
            throw new Exception("该用户不存在！");
        }
        return employee;
    }
}
