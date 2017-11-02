package com.yunip.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.enums.api.ApiException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.CodeType;
import com.yunip.enums.company.CpRoleType;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.DiskException;
import com.yunip.mapper.authority.ICpPermissionDao;
import com.yunip.mapper.authority.ICpRoleDao;
import com.yunip.mapper.authority.ICpRolePermissionDao;
import com.yunip.mapper.authority.IEmployeeRoleDao;
import com.yunip.mapper.company.IEmployeeDao;
import com.yunip.mapper.disk.IContactDao;
import com.yunip.mapper.disk.IEmployeeDiskDao;
import com.yunip.mapper.user.IAdminDao;
import com.yunip.mapper.user.IAdminRoleDao;
import com.yunip.model.authority.CpRole;
import com.yunip.model.authority.CpRolePermission;
import com.yunip.model.authority.EmployeeRole;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.Role;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.disk.Contact;
import com.yunip.model.disk.EmployeeDisk;
import com.yunip.model.disk.query.ContactQuery;
import com.yunip.model.user.Admin;
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
    private IEmployeeDao       employeeDao;
    
    @Resource(name = "iSerialCodeService")
    private ISerialCodeService serialCodeService;
    
    @Resource(name = "iAdminService")
    private IAdminService      adminService;
    
    @Resource(name = "iEmployeeDiskDao")
    private IEmployeeDiskDao   employeeDiskDao;
    
    @Resource(name = "iAdminRoleDao")
    private IAdminRoleDao       adminRoleDao;
    
    @Resource(name = "iCpPermissionDao")
    private ICpPermissionDao cpPermissionDao;
    
    @Resource(name = "iCpRoleDao")
    private ICpRoleDao cpRoleDao;
    
    @Resource(name = "iCpRolePermissionDao")
    private ICpRolePermissionDao cpRolePermissionDao;
    
    @Resource(name = "iEmployeeRoleDao")
    private IEmployeeRoleDao employeeRoleDao;
    
    @Resource(name = "iAdminDao")
    private IAdminDao           adminDao;
    
    @Resource(name = "iContactDao")
    private IContactDao contactDao;
    
    public void saveOrUpdate(Admin admin,String employeeName){
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
            if(employeeRoles != null && employeeRoles.size() > 0){
                for(EmployeeRole er : employeeRoles){
                    //r1:判断！ 如果是专属角色，上述三张表的相关数据全删；如果是角色包角色，只删除员工与角色对应关系
                    CpRole cpRole = cpRoleDao.selectById(er.getRoleId());
                    if(cpRole == null || cpRole.getId() == null){
                        continue;
                    }
                    if(cpRole.getRoleType().equals(CpRoleType.EXCLUSIVEROLE.getType())){
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
            cpRole.setRoleName(employeeName+"的专属角色");
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
            String pwd = Md5.encodeByMd5(admin.getAccountPwd());
            admin.setAccountPwd(pwd);
        }
        catch (Exception e) {}
        admin.setIsAdmin(IsAdminType.YG.getType());
        admin.setValidStatus(0);
        //管理员表，相关新增与修改
        saveOrUpdate(admin,employee.getEmployeeName());
        //员工添加
        String empChar = PinYinUtil.getFirstInitWord(employee.getEmployeeName()).toUpperCase();
        employee.setId(admin.getId());
        employee.setEmployeeChar(empChar);
        employee.setDeptId("00");
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
    
    @Override
    public Map<String, List<Employee>> getAllEmployees() {
        EmployeeQuery query = new EmployeeQuery();
        query.setPageSize(Integer.MAX_VALUE);
        query.setIsAdmin(IsAdminType.YG.getType());
        List<Employee> employees = employeeDao.selectByQuery(query);
        //分成MAP
        Map<String, List<Employee>> dataMap = new HashMap<String, List<Employee>>();
        if(employees != null){
            for(Employee employee : employees){
                if(dataMap.containsKey(employee.getEmployeeChar())){
                    List<Employee> emps = dataMap.get(employee.getEmployeeChar());
                    emps.add(employee);
                } else {
                    List<Employee> emps = new ArrayList<Employee>();
                    emps.add(employee);
                    dataMap.put(employee.getEmployeeChar(), emps);
                }
            }
        }
        return dataMap;
    }
    
    @Override
    public Map<String, List<Contact>> getAllContactEmployees(Integer employeeId) {
        ContactQuery query = new ContactQuery();
        query.setEmployeeId(employeeId);
        List<Contact> contacts = contactDao.selectByQuery(query);
        //分成MAP
        Map<String, List<Contact>> dataMap = new HashMap<String, List<Contact>>();
        if(contacts != null){
            for(Contact contact : contacts){
                if(dataMap.containsKey(contact.getEmployee().getEmployeeChar())){
                    List<Contact> conts = dataMap.get(contact.getEmployee().getEmployeeChar());
                    conts.add(contact);
                } else {
                    List<Contact> conts = new ArrayList<Contact>();
                    conts.add(contact);
                    dataMap.put(contact.getEmployee().getEmployeeChar(), conts);
                }
            }
        }
        return dataMap;
    }

    @Override
    public int updateEmployeeById(Employee employee) {
        return employeeDao.update(employee);
    }
    
    public void checkEmployeeCode(String empCode, Integer employeeId) {
        if (employeeId != null) {
            Employee empTmp = employeeDao.selectById(employeeId);
            if (empCode.equals(empTmp.getEmployeeCode())) {
                return;
            }
        }
        if (StringUtil.nullOrBlank(empCode)) {
            throw new MyException(DiskException.WSRBH);
        }
        EmployeeQuery query = new EmployeeQuery();
        query.setEmployeeCode(empCode);
        query.setIsAdmin(IsAdminType.YG.getType());
        Employee employee = employeeDao.selectEmpByQuery(query);
        if (employee != null) {
            throw new MyException(DiskException.YJCZGBH);
        }
    }

    public void checkEmployeeMobile(String mobile, Integer employeeId) {
        if (employeeId != null) {
            Employee empTmp = employeeDao.selectById(employeeId);
            if (mobile.equals(empTmp.getEmployeeMobile())) {
                return;
            }
        }
        if (StringUtil.nullOrBlank(mobile)) {
            throw new MyException(DiskException.WSRSJ);
        }
        //手机号格式验证
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
        Matcher m = p.matcher(mobile);
        if(!m.matches()){
            throw new MyException(DiskException.SJGSBZQ);
        }
        EmployeeQuery query = new EmployeeQuery();
        query.setEmployeeMobile(mobile);
        query.setIsAdmin(IsAdminType.YG.getType());
        Employee employee = employeeDao.selectEmpByQuery(query);
        if (employee != null) {
            throw new MyException(DiskException.YJCZGSJ);
        }
    }
    
    @Override
    public Employee selectEmpByQuery(EmployeeQuery query) {
        return employeeDao.selectEmpByQuery(query);
    }
    
    /**
     * 注册员工人数限制
     * checkRegisterLimit(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param type  
     * void 
     * @exception
     */
    public void checkRegisterLimit(int type,Integer intoNum){
        if (type == IsAdminType.YG.getType()) {
            //取出秘钥
            String key = SysConfigHelper.getValue(SystemContant.BASICSCODE, SystemContant.REGISTER_RESTRICTIONS_KEY);
            Des desObj = new Des();
            String macCode = SystemUtil.getConfig();
            String clientSign = "";
            try {
                clientSign = Md5.encodeByMd5(macCode) + SystemContant.LICENSECODEKEY;
            }
            catch (Exception e1) {
                throw new MyException(DiskException.KHDSCBM_MD5_ERROR);
            }
            String num = desObj.strDec(key, clientSign, null, null);
            //条件查询admin数量
            AdminQuery query = new AdminQuery();
            query.setIsAdmin(IsAdminType.YG.getType());
            query.setValidStatus(ValidStatus.NOMAL.getStatus());
            int employeeNum = adminDao.selectCountByQuery(query);
            employeeNum = intoNum == null ? employeeNum + 1 : employeeNum
                    + intoNum;
            try {
                int limitNum = Integer.valueOf(num);
                if(employeeNum > limitNum){
                    throw new MyException(DiskException.EXCEED_REGISTER_MAXIMUM);
                }
            }
            catch (NumberFormatException e) {
                throw new MyException(DiskException.REGISTER_KEY_ERROR);
            }
        }
    }
    
    @Override
    public Employee getEmployee(String token,String ip) {
        AdminQuery query = new AdminQuery();
        if (!StringUtils.isNotBlank(token)) {
            throw new MyException(ApiException.TOKENFAILURE);
        }
        //token解密
        Des des = new Des();
        String tokenStr = des.strDec(token, SystemContant.desKey, null, null);
        String[] tokenArr = tokenStr.split("\\|");
        if(tokenArr == null || tokenArr.length != 2){
            throw new MyException(ApiException.TOKENFAILURE);
        }
        //判断token时效性
        Long oldTime = Long.valueOf(tokenArr[1]);
        Long newTime = System.currentTimeMillis();
        if((newTime - oldTime) > SystemContant.TOKEN_TIMELINESS){
            throw new MyException(ApiException.TOKENFAILURE);
        }
        token = tokenArr[0];
        //先查询登录信息
        query.setToken(token);
        Admin admin = adminService.getLoginAdmin(query);
        if (admin == null) {
            throw new MyException(ApiException.TOKENFAILURE);
        }
        Employee employee = employeeDao.selectById(admin.getId());
        if (employee == null) {
            throw new MyException(ApiException.TOKENFAILURE);
        }
        employee.setLoginIp(ip);
        return employee;
    }
    
}
