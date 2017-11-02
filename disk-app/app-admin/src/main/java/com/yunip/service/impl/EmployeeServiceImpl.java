package com.yunip.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.CodeType;
import com.yunip.enums.company.CpRoleType;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.PersonalPermissionType;
import com.yunip.manage.FileManager;
import com.yunip.mapper.authority.ICpPermissionDao;
import com.yunip.mapper.authority.ICpRoleDao;
import com.yunip.mapper.authority.ICpRolePermissionDao;
import com.yunip.mapper.authority.IEmployeeRoleDao;
import com.yunip.mapper.company.IDepartmentDao;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.disk.IEmployeeDiskDao;
import com.yunip.mapper.user.IAdminDao;
import com.yunip.mapper.user.IAdminPermissionDao;
import com.yunip.mapper.user.IAdminRoleDao;
import com.yunip.model.authority.CpRole;
import com.yunip.model.authority.CpRolePermission;
import com.yunip.model.authority.EmployeeRole;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.Role;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.disk.EmployeeDisk;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminPermission;
import com.yunip.model.user.AdminRole;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IEmployeeService;
import com.yunip.service.ISerialCodeService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.PinYinUtil;
import com.yunip.utils.util.StringUtil;
import com.yunip.utils.util.SystemUtil;

/**
 * @author ming.zhu
 * 员工实现类
 */
@Service("iEmployeeService")
public class EmployeeServiceImpl implements IEmployeeService {

    @Resource(name = "iEmployeeDao")
    private IEmployeeDao         employeeDao;

    @Resource(name = "iSerialCodeService")
    private ISerialCodeService   serialCodeService;

    @Resource(name = "iAdminService")
    private IAdminService        adminService;

    @Resource(name = "iAdminDao")
    private IAdminDao            adminDao;

    @Resource(name = "iEmployeeDiskDao")
    private IEmployeeDiskDao     employeeDiskDao;

    @Resource(name = "iDepartmentDao")
    private IDepartmentDao       departmentDao;

    @Resource(name = "iAdminPermissionDao")
    private IAdminPermissionDao  adminPermissionDao;

    @Resource(name = "iAdminRoleDao")
    private IAdminRoleDao        adminRoleDao;

    @Resource(name = "iCpPermissionDao")
    private ICpPermissionDao     cpPermissionDao;

    @Resource(name = "iCpRoleDao")
    private ICpRoleDao           cpRoleDao;

    @Resource(name = "iCpRolePermissionDao")
    private ICpRolePermissionDao cpRolePermissionDao;

    @Resource(name = "iEmployeeRoleDao")
    private IEmployeeRoleDao     employeeRoleDao;
    
    @Resource(name = "fileManager")
    private FileManager fileManager;

    private void saveOrUpdate(Admin admin, String employeeName) {
        //开始更新
        if (admin.getId() == null) {
            admin.setValidStatus(1);
            adminDao.insert(admin);
        }
        else {
            adminDao.update(admin);
            //相关角色全部清空
            adminRoleDao.delByAdminId(admin.getId() == null ? 0 : admin.getId());
            //r1:相关权限全部清空：删除员工对应角色cpRole(判断)，对应角色关系employeeRole，及角色的对应权限关系cpRolePermission(判断)
            //r1: first 查询对应角色
            List<EmployeeRole> employeeRoles = employeeRoleDao.selectByEmployeeId(admin.getId());
            if (employeeRoles != null && employeeRoles.size() > 0) {
                for (EmployeeRole er : employeeRoles) {
                    //r1:判断！ 如果是专属角色，上述三张表的相关数据全删；如果是角色包角色，只删除员工与角色对应关系
                    CpRole cpRole = cpRoleDao.selectById(er.getRoleId());
                    if (cpRole == null || cpRole.getId() == null) {
                        continue;
                    }
                    if (cpRole.getRoleType().equals(
                            CpRoleType.EXCLUSIVEROLE.getType())) {
                        //r1:判断为专属角色 second 删除角色的对应权限关系
                        cpRolePermissionDao.delByRoleId(cpRole.getId());
                        //r1:third 删除角色
                        cpRoleDao.delById(cpRole.getId());
                    }
                    //r1:fourth 删除与该员工关联的角色
                    employeeRoleDao.delByEmployeeId(admin.getId());
                }
            }

        }
        if (admin.getId() != null && admin.getId() > 0
                && admin.getRoles() != null) {
            //添加角色与用户关系
            for (Role role : admin.getRoles()) {
                if (role.getId() == null) {
                    continue;
                }
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(admin.getId());
                adminRole.setRoleId(role.getId());
                adminRoleDao.insert(adminRole);
            }
        }
        if (admin.getPermissions() != null && admin.getPermissions().size() > 0) {
            //r1:创建专属个人角色
            CpRole cpRole = new CpRole();
            cpRole.setRoleType(CpRoleType.EXCLUSIVEROLE.getType());
            cpRole.setEmployeeId(admin.getId());
            cpRole.setValidStatus(ValidStatus.NOMAL.getStatus());
            cpRole.setRoleName(employeeName + "的专属角色");
            cpRoleDao.insert(cpRole);
            //r1:添加角色与权限关系
            for (Permission per : admin.getPermissions()) {
                if (per.getId() == null) {
                    continue;
                }
                CpRolePermission crp = new CpRolePermission();
                crp.setRoleId(cpRole.getId());
                crp.setPermissionId(per.getId());
                cpRolePermissionDao.insert(crp);
            }
            //r1:添加员工与角色关系，如此，员工通过角色拥有权限
            EmployeeRole employeeRole = new EmployeeRole();
            employeeRole.setRoleId(cpRole.getId());
            employeeRole.setEmployeeId(admin.getId());
            employeeRoleDao.insert(employeeRole);
        }
    }

    public int addEmployee(Employee employee, Admin admin) {
        serialCodeService.updateVersion(CodeType.EVERSION.getType());
        admin.setMobile(employee.getEmployeeMobile());
        try {
            String pwd = Md5.encodeByMd5("123456");
            admin.setAccountPwd(pwd);
        }
        catch (Exception e) {}
        //管理员表，相关新增与修改
        saveOrUpdate(admin, employee.getEmployeeName());
        //员工添加
        String empChar = PinYinUtil.getFirstInitWord(employee.getEmployeeName()).toUpperCase();
        employee.setId(admin.getId());
        employee.setEmployeeChar(empChar);
        employee.setValidStatus(1);
        //设置空间大小：调用空间规则
        fileManager.spaceRule(employee);
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
        //管理员表，相关新增与修改
        saveOrUpdate(admin, employee.getEmployeeName());
        //员工修改
        employee.setUpdateTime(new Date());
        //设置空间大小：调用空间规则
        fileManager.spaceRule(employee);
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

    public void checkEmployeeCode(String empCode, Integer employeeId,
            Integer isAdmin) {
        if ((IsAdminType.GLY.getType()).equals(isAdmin)) {
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

    public void checkEmployeeMobile(String mobile, Integer employeeId,
            Integer isAdmin) {
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
        Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$");
        Matcher m = p.matcher(mobile);
        if (!m.matches()) {
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
    public int importEmployees(List<Employee> employees, String deptId,
            Admin tdAdmin) throws Exception {
        if (employees != null && employees.size() > 0) {
            int index = 0;
            List<Department> departments = departmentDao.selectByAll();
            for (Employee employee : employees) {
                if (index > 1) {
                    if (StringUtils.isBlank(employee.getEmployeeMobile())
                            || StringUtils.isBlank(employee.getSex())
                            || StringUtils.isBlank(employee.getEmployeeCode())) {
                        throw new MyException(AdminException.BTZDWK);
                    }
                    Long phone = Long.valueOf(employee.getEmployeeMobile());
                    DecimalFormat decimalFormat = new DecimalFormat("#");
                    employee.setEmployeeMobile(decimalFormat.format(phone));
                    //先验证
                    AdminQuery adminQuery = new AdminQuery();
                    adminQuery.setMobile(employee.getEmployeeMobile());
                    List<Admin> admins = adminDao.selectByQuery(adminQuery);
                    if (admins != null && admins.size() > 0) {
                        throw new MyException(AdminException.YJCZGSJ,
                                employee.getEmployeeMobile());
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
                    Employee emps = employeeDao.selectEmpByQuery(employeeQuery);
                    if (emps != null) {
                        throw new MyException(AdminException.YJCZGBH,
                                employee.getEmployeeCode());
                    }
                    //添加员工
                    String empChar = PinYinUtil.getFirstInitWord(
                            employee.getEmployeeName()).toUpperCase();
                    employee.setEmployeeChar(empChar);
                    employee.setEmployeeSex("男".equals(employee.getSex()) ? 0 : 1);
                    employee.setId(admin.getId());
                    employee.setDeptId(deptId);
                    employee.setValidStatus(ValidStatus.NOMAL.getStatus());
                    employee.setCreateAdmin(tdAdmin.getEmployeeName());
                    employee.setCreateTime(new Date());
                    //查询部门名称（默认为最上级部门）
                    String empDeptId = "00";
                    if (StringUtils.isNotBlank(employee.getDeptName())) {
                        if (departments != null && departments.size() > 0) {
                            for (Department department : departments) {
                                if (department.getDeptName().equals(
                                        employee.getDeptName().trim())) {
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
                    //导入用户权限复制
                    insertAdminPermission(employee.getId());
                }
                index++;
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

    /**
     * 对指定员工赋予全部个人空间权限
     * @param adminId  
     * void 
     * @exception
     */
    private void insertAdminPermission(Integer adminId) {
        //赋予权限
        PersonalPermissionType[] personalPermissionTypes = PersonalPermissionType.values();
        for (PersonalPermissionType ppt : personalPermissionTypes) {
            AdminPermission adminPermission = new AdminPermission();
            adminPermission.setAdminId(adminId);
            adminPermission.setPermissionId(ppt.getType());
            adminPermissionDao.insert(adminPermission);
        }
    }

    /**
     * 注册员工人数限制
     * @param type  
     * void 
     * @exception
     */
    public void checkRegisterLimit(int type, Integer intoNum) {
        if (type == IsAdminType.YG.getType()) {
            int employeeNum = getUseingNum();
            employeeNum = intoNum == null ? employeeNum + 1 : employeeNum
                    + intoNum;
            try {
                int limitNum = getRegisterNum();
                if (employeeNum > limitNum) {
                    throw new MyException(
                            AdminException.EXCEED_REGISTER_MAXIMUM);
                }
            }
            catch (NumberFormatException e) {
                throw new MyException(AdminException.REGISTER_KEY_ERROR);
            }
        }
    }
    
    public int getRegisterNum(){
        int limitNum = 0;
        //取出秘钥
        String key = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                SystemContant.REGISTER_RESTRICTIONS_KEY);
        if(!StringUtil.nullOrBlank(key)){
            Des desObj = new Des();
            String macCode = SystemUtil.getConfig();
            String clientSign = "";
            try {
                clientSign = Md5.encodeByMd5(macCode) + SystemContant.LICENSECODEKEY;
            }
            catch (Exception e1) {
                throw new MyException(AdminException.KHDSCBM_MD5_ERROR);
            }
            String num = desObj.strDec(key, clientSign, null, null);
            
            try {
                limitNum = Integer.valueOf(num);
            } catch (NumberFormatException e) {
                //throw new MyException(AdminException.REGISTER_KEY_ERROR);
                limitNum = -1;
            }
        }
        return limitNum;
    }
    
    public int getUseingNum(){
        //条件查询admin数量
        AdminQuery query = new AdminQuery();
        query.setIsAdmin(IsAdminType.YG.getType());
        query.setValidStatus(ValidStatus.NOMAL.getStatus());
        int employeeNum = adminDao.selectCountByQuery(query);
        return employeeNum;
    }

}
