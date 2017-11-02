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
import com.yunip.enums.log.AdminActionType;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.Role;
import com.yunip.model.authority.RolePermission;
import com.yunip.model.authority.query.PermissionQuery;
import com.yunip.model.authority.query.RoleQuery;
import com.yunip.model.log.AdminLog;
import com.yunip.service.IAdminLogService;
import com.yunip.service.authority.IPermissionService;
import com.yunip.service.authority.IRoleService;
import com.yunip.web.common.Authority;

/**
 * @author ming.zhu
 * 在线分销管理平台用户相关操作（用户、角色、权限）
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    /**
     * 角色功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iRoleService")
    private IRoleService      roleService;
    
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
     * index(角色列表) 
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @Authority("P09")
    @RequestMapping("/index")
    public String index(HttpServletRequest request,RoleQuery query) {
        query.setSystemCode(SystemCode.OTM.getType());
        query = roleService.queryRoles(query);
        request.setAttribute("query", query);
        return "role/index";
    }

    /**
     * toInsertRolePage(跳转新增角色) 
     * @param request
     * @param roleId
     * @return  
     * String 
     * @exception
     */
    @Authority("P10")
    @RequestMapping("/toinsert/{roleId}")
    public String toInsertRolePage(HttpServletRequest request,@PathVariable Integer roleId) {
        //查询所有权限
        PermissionQuery query = new PermissionQuery();
        query.setValidStatus(ValidStatus.NOMAL.getStatus());
        query.setSystemCode(SystemCode.OTM.getType());
        List<Permission> permissions = permissionService.getParentPermissions(query);
        request.setAttribute("permissions", permissions);
        return "role/saveoredit";
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
    @Authority("P11")
    @RequestMapping("/toupdate/{roleId}")
    public String toUpdateRolePage(HttpServletRequest request,@PathVariable Integer roleId) {
        if(roleId != 0){
            //修改
            RoleQuery query = new RoleQuery();
            query.setId(roleId);
            query.setSystemCode(SystemCode.OTM.getType());
            Role role = roleService.queryRoles(query).getList().get(0);
            List<RolePermission> list =  roleService.getRolePermission(roleId);
            StringBuilder permissionStr = new StringBuilder();
            for(RolePermission rp : list){
                permissionStr.append(rp.getPermissionId()+",");
            }
            if(permissionStr.length()>0){
                permissionStr.deleteCharAt(permissionStr.length()-1);
            }
            request.setAttribute("permissionStr", permissionStr.toString());
            request.setAttribute("role", role);
        }
        //查询所有权限
        PermissionQuery query = new PermissionQuery();
        query.setSystemCode(SystemCode.OTM.getType());
        query.setValidStatus(ValidStatus.NOMAL.getStatus());
        List<Permission> permissions = permissionService.getParentPermissions(query);
        request.setAttribute("permissions", permissions);
        return "role/saveoredit";
    }
    
    /**
     * insertRole(保存角色) 
     * @param request
     * @param role
     * @return  
     * JsonData<String> 
     * @exception
     */
    @Authority("P10")
    @ResponseBody
    @RequestMapping("/save")
    public JsonData<String> insertRole(HttpServletRequest request,Role role) {
        JsonData<String> data = new JsonData<String>();
        role.setSystemCode(SystemCode.OTM.getType());
        role.setCreateAdmin(getMyinfo(request).getEmployeeName());
        if (!roleService.saveOrUpdate(role)) {
            throw new MyException(AdminException.ERROR);
        }
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
        adminLog.setOperContent("角色添加:"+role.getRoleName());
        adminLogService.addAdminLog(adminLog);
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
    @Authority("P11")
    @ResponseBody
    @RequestMapping("/edit")
    public JsonData<String> editRole(HttpServletRequest request,Role role) {
        JsonData<String> data = new JsonData<String>();
        role.setSystemCode(SystemCode.OTM.getType());
        role.setUpdateAdmin(getMyinfo(request).getEmployeeName());
        if (!roleService.saveOrUpdate(role)) {
            throw new MyException(AdminException.ERROR);
        }
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
        adminLog.setOperContent("角色修改:"+role.getRoleName());
        adminLogService.addAdminLog(adminLog);
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
    @Authority("P12")
    @ResponseBody
    @RequestMapping("/freeze/{roleId}")
    public JsonData<Integer> freeze(HttpServletRequest request,@PathVariable Integer roleId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        roleService.updateValidStatus(roleId, ValidStatus.FREEZE.getStatus(),SystemCode.OTM.getType());
        //日志添加
        Role role = roleService.getRole(roleId);
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
        adminLog.setOperContent("角色冻结:"+role.getRoleName());
        adminLogService.addAdminLog(adminLog);
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
    @Authority("P13")
    @ResponseBody
    @RequestMapping("/nomal/{roleId}")
    public JsonData<Integer> nomal(HttpServletRequest request,@PathVariable Integer roleId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        roleService.updateValidStatus(roleId, ValidStatus.NOMAL.getStatus(),SystemCode.OTM.getType());
        //日志添加
        Role role = roleService.getRole(roleId);
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
        adminLog.setOperContent("角色开启:"+role.getRoleName());
        adminLogService.addAdminLog(adminLog);
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
    @Authority("P14")
    @RequestMapping("/delete/{roleId}")
    @ResponseBody
    public JsonData<?> deleteRole(HttpServletRequest request,@PathVariable Integer roleId){
        JsonData<String> data = new JsonData<String>();
        Role role = roleService.getRole(roleId);
        int i = roleService.deleteRole(roleId, SystemCode.OTM.getType());
        if(i == 1){
            data.setCode(AdminException.USERINGROLE.getCode());
            data.setCodeInfo(AdminException.USERINGROLE.getMsg());
        }
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ROLEMANAGE.getType());
        adminLog.setOperContent("角色删除:"+role.getRoleName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }


}
