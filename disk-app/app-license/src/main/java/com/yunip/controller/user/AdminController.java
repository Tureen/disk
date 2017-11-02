package com.yunip.controller.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.SystemCode;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.log.AdminActionType;
import com.yunip.model.authority.query.RoleQuery;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.log.AdminLog;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminRole;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IAdminLogService;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.authority.IPermissionService;
import com.yunip.service.authority.IRoleService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.pwd.Md5;
import com.yunip.web.common.Authority;

/**
 * @author ming.zhu
 * 平台用户相关操作（用户)
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    /**
     * 用户功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminService")
    private IAdminService      adminService;
    
    /**
     * 部门功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iDepartmentService")
    private IDepartmentService      departmentService;
    
    /**
     * 员工功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;

    /**
     * 角色功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iRoleService")
    private IRoleService       roleService;

    /**
     * 权限功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iPermissionService")
    private IPermissionService permissionService;
    
    /**
     * 管理员or员工操作日志 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminLogService")
    private IAdminLogService adminLogService;

    /**
     * index(平台管理员列表) 
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @Authority("P03")
    @RequestMapping("/index")
    public String index(HttpServletRequest request, EmployeeQuery query) {
        query.setIsAdmin(IsAdminType.GLY.getType());
        query = employeeService.getEmployeeByQuery(query);
        RoleQuery queryTmp = new RoleQuery();
        queryTmp.setSystemCode(SystemCode.OTM.getType());
        queryTmp.setPageSize(Integer.MAX_VALUE);
        queryTmp = roleService.queryRoles(queryTmp);
        request.setAttribute("roleList", queryTmp.getList());
        request.setAttribute("query", query);
        request.setAttribute("adminType", IsAdminType.values());
        return "user/index";
    }
    
    /**
     * toInsertOrUpdateAdminPage(跳转新增管理员页面) 
     * @param request
     * @param adminId
     * @return  
     * String 
     * @exception
     */
    @Authority("P04")
    @RequestMapping("/toinsert/{isAdmin}")
    public String toInsertAdminPage(HttpServletRequest request,@PathVariable Integer isAdmin) {
        //所有角色获取
        RoleQuery queryTmp = new RoleQuery();
        queryTmp.setSystemCode(SystemCode.OTM.getType());
        queryTmp.setPageSize(Integer.MAX_VALUE);
        queryTmp = roleService.queryRoles(queryTmp);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("roleList", queryTmp.getList());
        request.setAttribute("departmentList", departmentList);
        return "user/saveoredit";
    }
    
    /**
     * toInsertOrUpdateAdminPage(跳转修改管理员页面) 
     * @param request
     * @param adminId
     * @return  
     * String 
     * @exception
     */
    @Authority("P05")
    @RequestMapping("/toupdate/{isAdmin}/{adminId}")
    public String toUpdateAdminPage(HttpServletRequest request,@PathVariable Integer isAdmin,@PathVariable Integer adminId) {
        if(adminId != 0){
            Employee employee = employeeService.getEmployeeById(adminId);
            AdminQuery query = new AdminQuery();
            query.setId(adminId);
            List<Admin> admins = adminService.queryAdmins(query).getList();
            List<AdminRole> list =  adminService.getAdminRole(adminId);
            StringBuilder roleStr = new StringBuilder();
            for(AdminRole ar : list){
                roleStr.append(ar.getRoleId()+",");
            }
            if(roleStr.length()>0){
                roleStr.deleteCharAt(roleStr.length()-1);
            }
            request.setAttribute("roleStr", roleStr.toString());
            request.setAttribute("admin", admins.size()>0?admins.get(0):null);
            request.setAttribute("employee", employee);
        }
        //所有角色获取
        RoleQuery queryTmp = new RoleQuery();
        queryTmp.setSystemCode(SystemCode.OTM.getType());
        queryTmp.setPageSize(Integer.MAX_VALUE);
        queryTmp = roleService.queryRoles(queryTmp);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("isAdmin", isAdmin);
        request.setAttribute("roleList", queryTmp.getList());
        request.setAttribute("departmentList", departmentList);
        return "user/saveoredit";
    }
    
    /**
     * insertAdmin(保存管理员) 
     * @param request
     * @param admin
     * @return  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/save")
    @Authority("P04")
    @ResponseBody
    public JsonData<?> insertAdmin(HttpServletRequest request,Employee employee,Admin admin) {
        JsonData<?> data = new JsonData<String>();
        employeeService.checkEmployeeMobile(employee.getEmployeeMobile(),null,admin.getIsAdmin());
        employeeService.checkEmployeeCode(employee.getEmployeeCode(),null,admin.getIsAdmin());
        employee.setCreateAdmin(getMyinfo(request).getEmployeeName());
        employeeService.addEmployee(employee,admin);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        if(admin.getIsAdmin()==IsAdminType.GLY.getType()){
            adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
            adminLog.setOperContent("添加管理员:"+employee.getEmployeeName());
        }else {
            adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
            adminLog.setOperContent("添加员工:"+employee.getEmployeeName());
        }
        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /**
     * editAdmin(修改管理员) 
     * @param request
     * @param admin
     * @return  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/edit")
    @Authority("P05")
    @ResponseBody
    public JsonData<?> editAdmin(HttpServletRequest request,Employee employee,Admin admin) {
        JsonData<?> data = new JsonData<String>();
        employeeService.checkEmployeeMobile(employee.getEmployeeMobile(),employee.getId(),admin.getIsAdmin());
        employeeService.checkEmployeeCode(employee.getEmployeeCode(),employee.getId(),admin.getIsAdmin());
        employee.setUpdateAdmin(getMyinfo(request).getEmployeeName());
        employeeService.updateEmployee(employee,admin);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        if(admin.getIsAdmin()==IsAdminType.GLY.getType()){
            adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
            adminLog.setOperContent("修改管理员:"+employee.getEmployeeName()+" 的资料");
        }else {
            adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
            adminLog.setOperContent("修改员工:"+employee.getEmployeeName()+" 的资料");
        }
        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /**
     * 冻结管理员
     * @param request
     * @param adminId
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @Authority("P06")
    @ResponseBody
    @RequestMapping("/freeze/{isAdmin}/{adminId}")
    public JsonData<Integer> freeze(HttpServletRequest request,@PathVariable Integer isAdmin,@PathVariable Integer adminId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        adminService.updateValidStatus(adminId, ValidStatus.FREEZE.getStatus());
        //日志添加
        Employee employee = employeeService.getEmployeeById(adminId);
        AdminLog adminLog = getAdminLog(request);
        if(isAdmin==IsAdminType.GLY.getType()){
            adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
            adminLog.setOperContent("冻结管理员:"+employee.getEmployeeName());
        }else {
            adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
            adminLog.setOperContent("冻结员工:"+employee.getEmployeeName());
        }
        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }

    /**
     * 解冻管理员
     * @param request
     * @param adminId
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @Authority("P07")
    @ResponseBody
    @RequestMapping("/nomal/{isAdmin}/{adminId}")
    public JsonData<Integer> nomal(HttpServletRequest request,@PathVariable Integer isAdmin,@PathVariable Integer adminId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        adminService.updateValidStatus(adminId, ValidStatus.NOMAL.getStatus());
        //日志添加
        Employee employee = employeeService.getEmployeeById(adminId);
        AdminLog adminLog = getAdminLog(request);
        if(isAdmin==IsAdminType.GLY.getType()){
            adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
            adminLog.setOperContent("开启管理员:"+employee.getEmployeeName());
        }else {
            adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
            adminLog.setOperContent("开启员工:"+employee.getEmployeeName());
        }
        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }
    
    /**
     * 删除管理员
     * @param request
     * @param adminId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @Authority("P08")
    @ResponseBody
    @RequestMapping("/delete/{isAdmin}/{adminId}")
    public JsonData<?> deleteAdmin(HttpServletRequest request,@PathVariable Integer isAdmin,@PathVariable Integer adminId){
        JsonData<String> data = new JsonData<String>();
        Employee employee = employeeService.getEmployeeById(adminId);
        employeeService.deleteEmployeeById(adminId);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        if(isAdmin==IsAdminType.GLY.getType()){
            adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
            adminLog.setOperContent("删除管理员:"+employee.getEmployeeName());
        }else {
            adminLog.setActionType(AdminActionType.EMPLOYEEMANAGE.getType());
            adminLog.setOperContent("删除员工:"+employee.getEmployeeName());
        }
        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /**
     * 重置密码
     * @throws Exception 
     * @param request
     * @param adminId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @Authority("P22")
    @ResponseBody
    @RequestMapping("/initPwd/{adminId}")
    public JsonData<?> initPwd(HttpServletRequest request,@PathVariable Integer adminId) throws Exception{
        JsonData<String> data = new JsonData<String>();
        AdminQuery query = new AdminQuery();
        query.setId(adminId);
        Admin admin = adminService.getLoginAdmin(query);
        String pwd = Md5.encodeByMd5("123456");
        admin.setAccountPwd(pwd);
        adminService.updateAdmin(admin);
        //日志添加
        Employee employee = employeeService.getEmployeeById(adminId);
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.EMPLOYEEINITPASSWD.getType());
        adminLog.setOperContent("员工密码重置:"+employee.getEmployeeName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /**
     * 跳转修改密码
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/touppasswd")
    public String toUpdatePass(HttpServletRequest request){
        Admin admin = getMyinfo(request);
        request.setAttribute("adminId", admin.getId());
        return "/user/uppasswd";
    }
    
    /**
     * 修改密码
     * @param request
     * @param password
     * @return  
     * JsonData<?> 
     * @exception
     */
    public JsonData<?> updatePass(HttpServletRequest request,
            HttpServletResponse response, String password, Integer adminId) {
        JsonData<?> data = new JsonData<String>();
        Admin admin = new Admin();
        admin.setId(adminId);
        admin.setAccountPwd(password);
        adminService.updateAdmin(admin);
        //清除session中的employee
        super.clearMyinfo(request);
        return data;
    }

    /**
     * 核验密码
     * @throws Exception 
     * @param request
     * @param password
     * @param newPassword
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/check")
    @ResponseBody
    public JsonData<?> checkPass(HttpServletRequest request,
            HttpServletResponse response, String password, String newPassword,
            String confirmPassword, Integer adminId) throws Exception {
        JsonData<?> data = new JsonData<String>();
        if (password == null || "".equals(password)) {
            throw new MyException(AdminException.NOPASSWORD);
        }
        if (newPassword == null || "".equals(newPassword)) {
            throw new MyException(AdminException.NONEWPASSWORD);
        }
        if (confirmPassword == null || "".equals(confirmPassword)) {
            throw new MyException(AdminException.NOCONFIRMPASSWORD);
        }
        if (!confirmPassword.equals(newPassword)) {
            throw new MyException(AdminException.NOSAMEPASS);
        }
        AdminQuery query = new AdminQuery();
        query.setPassword(Md5.encodeByMd5(password));
        query.setId(adminId);
        query = adminService.queryAdmins(query);
        List<Admin> list = query.getList();
        if (list.size() == 0) {
            //旧密码核验失败，数据库与之对应的用户密码不一致
            throw new MyException(DiskException.NOPASSCHECKPASSWORD);
        }
        //修改密码
        data = updatePass(request, response, Md5.encodeByMd5(newPassword),adminId);
        return data;
    }
}
