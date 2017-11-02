package com.yunip.controller.user;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.yunip.enums.company.CpRoleType;
import com.yunip.enums.company.IsAdminType;
import com.yunip.model.authority.CpPermission;
import com.yunip.model.authority.CpRole;
import com.yunip.model.authority.CpRolePermission;
import com.yunip.model.authority.query.CpRoleQuery;
import com.yunip.model.company.Department;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.service.ICpPermissionService;
import com.yunip.service.ICpRoleService;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;

/**
 * @author ming.zhu
 * 平台用户相关操作（用户)
 */
@Controller
@RequestMapping("/cprole")
public class CpRoleController extends BaseController{
    
    @Resource(name = "iCpRoleService")
    private ICpRoleService cpRoleService;
    
    @Resource(name = "iCpPermissionService")
    private ICpPermissionService cpPermissionService;
    
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
     * 员工角色管理界面
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,CpRoleQuery query){
        query.setRoleType(CpRoleType.DEFAULTROLE.getType());
        query = cpRoleService.queryCpRole(query);
        request.setAttribute("query", query);
        return "cprole/index";
    }
    
    /**
     * toInsertRolePage(跳转新增角色) 
     * @param request
     * @param roleId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toinsert/{roleId}")
    public String toInsertRolePage(HttpServletRequest request,@PathVariable Integer roleId) {
        //查询所有权限
        List<CpPermission> permissions = cpPermissionService.getPermissions();
        request.setAttribute("permissions", permissions);
        return "cprole/saveoredit";
    }
    
    /**
     * toUpdateRolePage(跳转修改角色) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request
     * @param roleId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toupdate/{roleId}")
    public String toUpdateRolePage(HttpServletRequest request,@PathVariable Integer roleId) {
        if(roleId != 0){
            //修改
            CpRole role = cpRoleService.getCpRole(roleId);
            List<CpRolePermission> list =  cpRoleService.getCpRolePermission(roleId);
            StringBuilder permissionStr = new StringBuilder();
            for(CpRolePermission rp : list){
                permissionStr.append(rp.getPermissionId()+",");
            }
            if(permissionStr.length()>0){
                permissionStr.deleteCharAt(permissionStr.length()-1);
            }
            request.setAttribute("permissionStr", permissionStr.toString());
            request.setAttribute("role", role);
        }
        //查询所有权限
        List<CpPermission> permissions = cpPermissionService.getPermissions();
        request.setAttribute("permissions", permissions);
        return "cprole/saveoredit";
    }
    
    /**
     * insertRole(保存角色) 
     * @param request
     * @param role
     * @return  
     * JsonData<String> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/save")
    public JsonData<String> insertRole(HttpServletRequest request,CpRole role) {
        JsonData<String> data = new JsonData<String>();
        role.setSystemCode(SystemCode.OTM.getType());
        role.setCreateAdmin(getMyinfo(request).getEmployeeName());
        role.setRoleType(CpRoleType.DEFAULTROLE.getType());
        if (!cpRoleService.saveOrUpdate(role)) {
            throw new MyException(AdminException.ERROR);
        }
        //日志添加
//        AdminLog adminLog = getAdminLog(request);
//        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
//        adminLog.setOperContent("角色添加:"+role.getRoleName());
//        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /**
     * editRole(修改) 
     * @param request
     * @param role
     * @return  
     * JsonData<String> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/edit")
    public JsonData<String> editRole(HttpServletRequest request,CpRole role) {
        JsonData<String> data = new JsonData<String>();
        role.setUpdateAdmin(getMyinfo(request).getEmployeeName());
        if (!cpRoleService.saveOrUpdate(role)) {
            throw new MyException(AdminException.ERROR);
        }
        //日志添加
//        AdminLog adminLog = getAdminLog(request);
//        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
//        adminLog.setOperContent("角色修改:"+role.getRoleName());
//        adminLogService.addAdminLog(adminLog);
        return data;
    }

    /**
     * freeze(冻结角色) 
     * @param request
     * @param roleId
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/freeze/{roleId}")
    public JsonData<Integer> freeze(HttpServletRequest request,@PathVariable Integer roleId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        cpRoleService.updateValidStatus(roleId, ValidStatus.FREEZE.getStatus());
        //日志添加
//        Role role = roleService.getRole(roleId);
//        AdminLog adminLog = getAdminLog(request);
//        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
//        adminLog.setOperContent("角色冻结:"+role.getRoleName());
//        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }

    /**
     * 
     * nomal(解冻角色) 
     * @param request
     * @param roleId
     * @return  
     * JsonData<Integer> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/nomal/{roleId}")
    public JsonData<Integer> nomal(HttpServletRequest request,@PathVariable Integer roleId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        cpRoleService.updateValidStatus(roleId, ValidStatus.NOMAL.getStatus());
        //日志添加
//        Role role = roleService.getRole(roleId);
//        AdminLog adminLog = getAdminLog(request);
//        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
//        adminLog.setOperContent("角色开启:"+role.getRoleName());
//        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }
    
    /**
     * 删除角色
     * @param request
     * @param roleId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/delete/{roleId}")
    @ResponseBody
    public JsonData<?> deleteRole(HttpServletRequest request,@PathVariable Integer roleId){
        JsonData<String> data = new JsonData<String>();
        CpRole role = cpRoleService.getCpRole(roleId);
        int i = cpRoleService.deleteRole(roleId);
        if(i == 1){
            data.setCode(AdminException.USERINGROLE.getCode());
            data.setCodeInfo(AdminException.USERINGROLE.getMsg());
        }
        //日志添加
//        AdminLog adminLog = getAdminLog(request);
//        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
//        adminLog.setOperContent("角色删除:"+role.getRoleName());
//        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
    /***
     * 部门树列表
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/tree")
    public String tree(HttpServletRequest request) {
        List<Department> list = departmentService.getAllDeparts();
        String roleId = request.getParameter("roleId");
        request.setAttribute("roleId", roleId);
        request.setAttribute("depts", list);
        return "cprole/tree";
    }
    
    /**
     * 部门树关联右侧员工列表
     * @param request
     * @param deptId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/treeright/{deptId}")
    public String treeRight(HttpServletRequest request, @PathVariable String deptId,EmployeeQuery query) {
        query.setId(null);
        query.setDeptId(deptId);
        query.setIsAdmin(IsAdminType.YG.getType());
        query.setPageSize(Integer.MAX_VALUE);
        query = employeeService.getEmployeeByQuery(query);
        Department department = departmentService.getDepartmentById(deptId);
        Integer roleId = 0;
        try {
            Object obj = request.getParameter("roleId");
            if(obj!=null){
                roleId = Integer.valueOf(""+obj);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String employeeIdStr = cpRoleService.getEmployeeIdStrByRoleId(roleId);
        request.setAttribute("employeeIdStr", employeeIdStr);
        request.setAttribute("roleId", roleId);
        request.setAttribute("department", department);
        request.setAttribute("query", query);
        return "cprole/treeright";
    }
    
    /**
     * insertEmployeeRole(保存角色对应员工) 
     * @param request
     * @param role
     * @return  
     * JsonData<String> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/saveemployee")
    public JsonData<String> insertEmployeeRole(HttpServletRequest request,CpRole role) {
        JsonData<String> data = new JsonData<String>();
        cpRoleService.saveEmployeeRoles(role.getId(), role.getEmployees(),role.getAllEmployees());
        //日志添加
//        AdminLog adminLog = getAdminLog(request);
//        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
//        adminLog.setOperContent("角色添加:"+role.getRoleName());
//        adminLogService.addAdminLog(adminLog);
        return data;
    }
    
}
