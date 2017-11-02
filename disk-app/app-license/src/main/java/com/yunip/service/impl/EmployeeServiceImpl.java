package com.yunip.service.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.constant.MyException;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.CodeType;
import com.yunip.enums.company.IsAdminType;
import com.yunip.mapper.company.IDepartmentDao;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.disk.IEmployeeDiskDao;
import com.yunip.mapper.user.IAdminDao;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.disk.EmployeeDisk;
import com.yunip.model.user.Admin;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IEmployeeService;
import com.yunip.service.ISerialCodeService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.PinYinUtil;
import com.yunip.utils.util.StringUtil;

/**
 * @author ming.zhu
 * 员工实现类
 */
@Service("iEmployeeService")
public class EmployeeServiceImpl implements IEmployeeService {

    @Resource(name = "iEmployeeDao")
    private IEmployeeDao       employeeDao;

    @Resource(name = "iSerialCodeService")
    private ISerialCodeService serialCodeService;

    @Resource(name = "iAdminService")
    private IAdminService      adminService;

    @Resource(name = "iAdminDao")
    private IAdminDao          adminDao;

    @Resource(name = "iEmployeeDiskDao")
    private IEmployeeDiskDao   employeeDiskDao;
    
    @Resource(name = "iDepartmentDao")
    private IDepartmentDao departmentDao;

    public int addEmployee(Employee employee, Admin admin) {
        serialCodeService.updateVersion(CodeType.EVERSION.getType());
        admin.setMobile(employee.getEmployeeMobile());
        try {
            String pwd = Md5.encodeByMd5("123456");
            admin.setAccountPwd(pwd);
        }
        catch (Exception e) {}
        adminService.saveOrUpdate(admin);
        //员工添加
        String empChar = PinYinUtil.getFirstInitWord(employee.getEmployeeName()).toUpperCase();
        employee.setId(admin.getId());
        employee.setEmployeeChar(empChar);
        employee.setValidStatus(1);
        employeeDao.insert(employee);
        //网盘信息添加
        EmployeeDisk disk = new EmployeeDisk();
        disk.setId(employee.getId());
        disk.setCreateAdmin(employee.getEmployeeName());
        disk.setUpdateAdmin(employee.getEmployeeName());
        disk.setDiskSize(0L);
        disk.setFolderNumber(0);
        disk.setFileNumer(0);
        disk.setReceiveFolderNumber(0);
        disk.setReceiveFileNumber(0);
        disk.setSignNumber(0);
        disk.setTakeFileNumber(0);
        employeeDiskDao.insert(disk);
        return 1;
    }

    public int updateEmployee(Employee employee, Admin admin) {
        serialCodeService.updateVersion(CodeType.EVERSION.getType());
        admin.setId(employee.getId());
        admin.setMobile(employee.getEmployeeMobile());
        adminService.saveOrUpdate(admin);
        //员工修改
        employee.setUpdateTime(new Date());
        employeeDao.update(employee);
        return 1;
    }

    public EmployeeQuery getEmployeeByQuery(EmployeeQuery query) {
        List<Employee> employees = employeeDao.selectByQuery(query);
        int count = employeeDao.selectCountByQuery(query);
        query.setList(employees);
        query.setRecordCount(count);
        return query;
    }

    public Employee getEmployeeById(Integer employeeId) {
        return employeeDao.selectById(employeeId);
    }

    public void checkEmployeeCode(String empCode, Integer employeeId, Integer isAdmin) {
        if((IsAdminType.GLY.getType()).equals(isAdmin)){
            return;
        }
        if (employeeId != null) {
            Employee empTmp = employeeDao.selectById(employeeId);
            if (empCode.equals(empTmp.getEmployeeCode())) {
                return;
            }
        }
        if (StringUtil.nullOrBlank(empCode)) {
            throw new MyException(AdminException.WSRBH);
        }
        EmployeeQuery query = new EmployeeQuery();
        query.setEmployeeCode(empCode);
        query.setIsAdmin(isAdmin);
        Employee employee = employeeDao.selectEmpByQuery(query);
        if (employee != null) {
            throw new MyException(AdminException.YJCZGBH);
        }
    }

    public void checkEmployeeMobile(String mobile, Integer employeeId, Integer isAdmin) {
        if (employeeId != null) {
            Employee empTmp = employeeDao.selectById(employeeId);
            if (mobile.equals(empTmp.getEmployeeMobile())) {
                return;
            }
        }
        if (StringUtil.nullOrBlank(mobile)) {
            throw new MyException(AdminException.WSRSJ);
        }
        //手机号格式验证
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
        Matcher m = p.matcher(mobile);
        if(!m.matches()){
            throw new MyException(AdminException.SJGSBZQ);
        }
        EmployeeQuery query = new EmployeeQuery();
        query.setEmployeeMobile(mobile);
        query.setIsAdmin(isAdmin);
        Employee employee = employeeDao.selectEmpByQuery(query);
        if (employee != null) {
            throw new MyException(AdminException.YJCZGSJ);
        }
    }

    public int changeStatus(Employee employee) {
        serialCodeService.updateVersion(CodeType.EVERSION.getType());
        return employeeDao.update(employee);
    }

    public int initPwd(Integer employeeId, String initPwd) {
        Admin admin = new Admin();
        admin.setId(employeeId);
        admin.setAccountPwd(initPwd);
        return adminDao.update(admin);
    }

    @Transactional
    public int changeMobile(Integer employeeId, String mobile) {
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setEmployeeMobile(mobile);
        employeeDao.update(employee);
        Admin admin = new Admin();
        admin.setId(employeeId);
        admin.setMobile(mobile);
        adminDao.update(admin);
        return 1;
    }

    @Override
    public int deleteEmployeeById(Integer employeeId) {
        employeeDao.delById(employeeId);
        adminService.deleteAdmin(employeeId);
        return 1;
    }

    @Override
    @Transactional
    public int importEmployees(List<Employee> employees, String deptId, Admin tdAdmin) throws Exception {
        if(employees != null && employees.size() > 0){
            int index = 0;
            List<Department> departments = departmentDao.selectByAll();
            for(Employee employee : employees){
                if(index > 1){
                    if(StringUtils.isBlank(employee.getEmployeeMobile()) || StringUtils.isBlank(employee.getSex())
                            || StringUtils.isBlank(employee.getEmployeeCode()) ){
                         throw new MyException(AdminException.BTZDWK);
                     }
                     //先验证
                     AdminQuery adminQuery = new AdminQuery();
                     adminQuery.setMobile(employee.getEmployeeMobile());
                     List<Admin> admins = adminDao.selectByQuery(adminQuery);
                     if(admins != null && admins.size() > 0){
                         throw new MyException(AdminException.YJCZGSJ, employee.getEmployeeMobile());
                     }
                     //先添加管理员
                     Admin admin = new Admin();
                     admin.setMobile(employee.getEmployeeMobile());
                     admin.setAccountPwd(Md5.encodeByMd5("123456"));
                     admin.setValidStatus(ValidStatus.NOMAL.getStatus());
                     admin.setIsAdmin(IsAdminType.YG.getType());
                     adminDao.insert(admin);
                     //先验证员工编号
                     EmployeeQuery employeeQuery = new EmployeeQuery();
                     employeeQuery.setEmployeeCode(employee.getEmployeeCode());
                     List<Employee> emps = employeeDao.selectByQuery(employeeQuery);
                     if(emps != null && emps.size() > 0){
                         throw new MyException(AdminException.YJCZGBH, employee.getEmployeeCode());
                     }
                     //添加员工
                     String empChar = PinYinUtil.getFirstInitWord(employee.getEmployeeName()).toUpperCase();
                     employee.setEmployeeChar(empChar);
                     employee.setEmployeeSex(employee.getSex() == "男" ?  0 : 1 );
                     employee.setId(admin.getId());
                     employee.setDeptId(deptId);
                     employee.setValidStatus(ValidStatus.NOMAL.getStatus());
                     employee.setCreateAdmin(tdAdmin.getEmployeeName());
                     employee.setCreateTime(new Date());
                     //查询部门名称（默认为最上级部门）
                     String empDeptId = "00";
                     if(StringUtils.isNotBlank(employee.getDeptName())){
                         if(departments != null && departments.size() > 0){
                             for(Department department : departments){
                                 if(department.getDeptName().equals(employee.getDeptName().trim())){
                                     empDeptId = department.getId();
                                 }
                             }  
                          }
                     }
                     employee.setDeptId(empDeptId);
                     employeeDao.insert(employee);
                     //网盘信息添加
                     EmployeeDisk disk = new EmployeeDisk();
                     disk.setId(employee.getId());
                     disk.setCreateAdmin(employee.getEmployeeName());
                     disk.setUpdateAdmin(employee.getEmployeeName());
                     disk.setDiskSize(0L);
                     disk.setFolderNumber(0);
                     disk.setFileNumer(0);
                     disk.setReceiveFolderNumber(0);
                     disk.setReceiveFileNumber(0);
                     disk.setSignNumber(0);
                     disk.setTakeFileNumber(0);
                     employeeDiskDao.insert(disk);
                }
                index ++ ;
            }
        }
        return 0;
    }

    @Override
    public Employee getEmployeeByCodeMobile(String employeeMobile,
            String employeeCode) {
        EmployeeQuery query = new EmployeeQuery();
        query.setEmployeeMobile(employeeMobile);
        query.setEmployeeCode(employeeCode);
        query.setIsAdmin(IsAdminType.YG.getType());
        query.setAdminValidStatus(ValidStatus.NOMAL.getStatus());
        return employeeDao.selectEmpByQuery(query);
    }

}
