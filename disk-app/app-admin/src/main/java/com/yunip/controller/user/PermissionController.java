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
import com.yunip.enums.PermissionType;
import com.yunip.enums.SystemCode;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.log.AdminActionType;
import com.yunip.model.authority.Permission;
import com.yunip.model.authority.query.PermissionQuery;
import com.yunip.model.log.AdminLog;
import com.yunip.service.IAdminLogService;
import com.yunip.service.authority.IPermissionService;
import com.yunip.web.common.Authority;

/**
 * @author ming.zhu
 * 在线分销管理平台用户相关操作（用户、角色、权限）
 */
@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController{
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
     * index(权限列表) 
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @Authority("P15")
    @RequestMapping("/index")
    public String index(HttpServletRequest request, PermissionQuery query) {
        query.setSystemCode(SystemCode.OTM.getType());
        query = permissionService.queryPermissions(query);
        request.setAttribute("query", query);
        request.setAttribute("enum", PermissionType.values());
        return "permission/index";
    }

    /**
     * treePermission(跳转新增权限的左侧节点树) 
     * @param request
     * @return  
     * String 
     * @exception
     */
    @Authority("P16")
    @RequestMapping("/treepermission")
    public String treePermission(HttpServletRequest request) {
        PermissionQuery query = new PermissionQuery();
        query.setSystemCode(SystemCode.OTM.getType());
        query.setValidStatus(1);
        query.setPageSize(Integer.MAX_VALUE);
        query = permissionService.queryPermissions(query);
        List<Permission> allfuncs = query.getList();
        request.setAttribute("allfuncs", allfuncs);
        return "permission/tree";
    }

    /**
     * toInsertPermission(跳转新增权限的右侧页面) 
     * @param request
     * @param permissionFid
     * @return  
     * String 
     * @exception
     */
    @Authority("P16")
    @RequestMapping("/toinsertpermission/{permissionFid}")
    public String toInsertPermission(HttpServletRequest request,@PathVariable Integer permissionFid) {
        Permission permissionParent = permissionService.getPermission(permissionFid, SystemCode.OTM.getType());
        if(permissionParent.getPermissionType()==3){
            return "common/404";
        }
        request.setAttribute("enum", PermissionType.values());
        request.setAttribute("permissionParent", permissionParent);
        return "permission/add";
    }
    
    /**
     * toUpdatePermission(跳转修改权限) 
     * @param request
     * @param permissionId
     * @return  
     * String 
     * @exception
     */
    @Authority("P17")
    @RequestMapping("/toupdatepermission/{permissionId}")
    public String toUpdatePermission(HttpServletRequest request,@PathVariable Integer permissionId) {
        Permission permission = permissionService.getPermission(permissionId, SystemCode.OTM.getType());
        if(permission.getPermissionFid()!=0){
            Permission permissionParent = permissionService.getPermission(permission.getPermissionFid(), SystemCode.OTM.getType());
            permission.setPermissionFname(permissionParent.getPermissionName());
        }
        PermissionQuery query = new PermissionQuery();
        query.setSystemCode(SystemCode.OTM.getType());
        query.setValidStatus(1);
        query.setPageSize(Integer.MAX_VALUE);
        query = permissionService.queryPermissions(query);
        List<Permission> allfuncs = query.getList();
        request.setAttribute("allfuncs", allfuncs);
        request.setAttribute("permission", permission);
        request.setAttribute("enum", PermissionType.values());
        return "permission/edit";
    }

    /**
     * savePermission(新增权限) 
     * @param request
     * @param permission
     * @return  
     * JsonData<?> 
     * @exception
     */
    @Authority("P16")
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> savePermission(HttpServletRequest request, Permission permission) {
        JsonData<Permission> data = new JsonData<Permission>();
        permission.setSystemCode(SystemCode.OTM.getType());
        int sub = permissionService.savePermission(permission);
        if (sub <= 0) {
            throw new MyException(AdminException.ERROR);
        }
        return data;
    }
    
    /**
     * editPermission(修改权限) 
     * @param request
     * @param permission
     * @return  
     * JsonData<?> 
     * @exception
     */
    @Authority("P17")
    @RequestMapping("/edit")
    @ResponseBody
    public JsonData<?> editPermission(HttpServletRequest request, Permission permission) {
        JsonData<Permission> data = new JsonData<Permission>();
        permission.setSystemCode(SystemCode.OTM.getType());
        int sub = permissionService.savePermission(permission);
        if (sub <= 0) {
            throw new MyException(AdminException.ERROR);
        }
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.PERMISSIONMANAGE.getType());
        adminLog.setOperContent("权限修改:"+permission.getPermissionName());
        adminLogService.addAdminLog(adminLog);
        return data;
    }

    /**
     * freeze(冻结权限) 
     * @param request
     * @param permissionId
     * @return  
     * String 
     * @exception
     */
    @Authority("P18")
    @ResponseBody
    @RequestMapping("/freeze/{permissionId}")
    public JsonData<?> freeze(HttpServletRequest request, @PathVariable Integer permissionId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        permissionService.updateValidStatus(permissionId, ValidStatus.FREEZE.getStatus(), SystemCode.OTM.getType());
        //日志添加
        Permission permission = permissionService.getPermission(permissionId, SystemCode.OTM.getType());
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.PERMISSIONMANAGE.getType());
        adminLog.setOperContent("权限冻结:"+permission.getPermissionName());
        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }

    /**
     * nomal(解冻权限) 
     * @param request
     * @param permissionId
     * @return  
     * String 
     * @exception
     */
    @Authority("P19")
    @ResponseBody
    @RequestMapping("/nomal/{permissionId}")
    public JsonData<?> nomal(HttpServletRequest request, @PathVariable Integer permissionId) {
        JsonData<Integer> jsonData = new JsonData<Integer>();
        permissionService.updateValidStatus(permissionId, ValidStatus.NOMAL.getStatus(), SystemCode.OTM.getType());
        //日志添加
        Permission permission = permissionService.getPermission(permissionId, SystemCode.OTM.getType());
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.PERMISSIONMANAGE.getType());
        adminLog.setOperContent("权限开启:"+permission.getPermissionName());
        adminLogService.addAdminLog(adminLog);
        return jsonData;
    }
}
