package com.yunip.controller.system;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.log.AdminActionType;
import com.yunip.model.config.ClusterConfig;
import com.yunip.model.config.query.ClusterConfigQuery;
import com.yunip.model.log.AdminLog;
import com.yunip.service.IAdminLogService;
import com.yunip.service.IClusterService;
import com.yunip.service.ICpPermissionService;
import com.yunip.service.ICpRoleService;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.authority.IPermissionService;
import com.yunip.service.authority.IRoleService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.http.HttpUtil;

/**
 * @author ming.zhu
 * 平台用户相关操作（用户)
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController extends BaseController {

    /**
     * 用户功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iAdminService")
    private IAdminService      adminService;

    /**
     * 部门功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;

    /**
     * 员工功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iEmployeeService")
    private IEmployeeService   employeeService;

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
    private IAdminLogService   adminLogService;
    
    /**
     * 员工角色Service
     */
    @Resource(name = "iCpRoleService")
    private ICpRoleService cpRoleService;
    
    /**
     * 员工权限Service
     */
    @Resource(name = "iCpPermissionService")
    private ICpPermissionService cpPermissionService;
    
    @Resource(name = "iClusterService")
    private IClusterService clusterService;

    /**
     * index(文件服务器列表) 
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, ClusterConfigQuery query) {
        query = clusterService.queryCluster(query);
        request.setAttribute("query", query);
        return "cluster/index";
    }

    /**
     * toInsertOrUpdateAdminPage(跳转新增文件服务器页面) 
     * @param request
     * @param adminId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toinsert")
    public String toInsertAdminPage(HttpServletRequest request) {
        return "cluster/saveoredit";
    }

    /**
     * toInsertOrUpdateAdminPage(跳转修改文件服务器页面) 
     * @param request
     * @param adminId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toupdate/{clusterId}")
    public String toUpdateAdminPage(HttpServletRequest request, @PathVariable Integer clusterId) {
        ClusterConfig clusterConfig = clusterService.getClusterById(clusterId);
        request.setAttribute("cluster", clusterConfig);
        return "cluster/saveoredit";
    }

    /**
     * insertAdmin(保存文件服务器) 
     * @param request
     * @param admin
     * @return  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> insertAdmin(HttpServletRequest request,ClusterConfig clusterConfig) {
        JsonData<?> data = new JsonData<String>();
        clusterService.addCluster(clusterConfig);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
        adminLog.setOperContent("添加文件服务器:" + clusterConfig.getClusterName());
        adminLogService.addAdminLog(adminLog);
        //重载文件服务器配置
        reloadClusterHelper();
        return data;
    }

    /**
     * editAdmin(修改文件服务器) 
     * @param request
     * @param admin
     * @return  
     * JsonData<String> 
     * @exception
     */
    @RequestMapping("/edit")
    @ResponseBody
    public JsonData<?> editAdmin(HttpServletRequest request,ClusterConfig clusterConfig) {
        JsonData<?> data = new JsonData<String>();
        clusterService.updateCluster(clusterConfig);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
        adminLog.setOperContent("修改文件服务器:" + clusterConfig.getClusterName());
        adminLogService.addAdminLog(adminLog);
        //重载文件服务器配置
        reloadClusterHelper();
        return data;
    }

    /**
     * 删除文件服务器
     * @param request
     * @param adminId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @ResponseBody
    @RequestMapping("/delete/{clusterId}")
    public JsonData<?> deleteAdmin(HttpServletRequest request,  @PathVariable Integer clusterId) {
        JsonData<?> data = new JsonData<String>();
        clusterService.delCluster(clusterId);
        //日志添加
        AdminLog adminLog = getAdminLog(request);
        adminLog.setActionType(AdminActionType.ADMINMANAGE.getType());
        adminLog.setOperContent("删除文件服务器");
        adminLogService.addAdminLog(adminLog);
        //重载文件服务器配置
        reloadClusterHelper();
        return data;
    }
    
    //重载文件服务器配置
    private void reloadClusterHelper(){
        ClusterConfigQuery query = new ClusterConfigQuery();
        query.setPageSize(Integer.MAX_VALUE);
        query = clusterService.queryCluster(query);
        if(query != null && query.getList() != null && query.getList().size() > 0){
            List<ClusterConfig> list = query.getList();
            for(ClusterConfig clusterConfig : list){
                HttpUtil.post(
                        (clusterConfig.getFileUrl() + SystemContant.RELOAD_CLUSTER_PATH), "", "", "UTF-8");
            }
        }
    }

}
