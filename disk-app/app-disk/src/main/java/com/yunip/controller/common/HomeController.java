package com.yunip.controller.common;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.disk.SpaceType;
import com.yunip.enums.disk.WorkgroupApplyStatus;
import com.yunip.manage.FileManager;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.EmployeeDisk;
import com.yunip.model.disk.query.WorkgroupApplyQuery;
import com.yunip.service.IContactService;
import com.yunip.service.IEmployeeDiskService;
import com.yunip.service.IMessageService;
import com.yunip.service.ISysConfigService;
import com.yunip.service.IWorkgroupApplyService;
import com.yunip.service.IWorkgroupService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Resource(name = "iAdminService")
    private IAdminService adminService;
    
    @Resource(name = "iEmployeeDiskService")
    private IEmployeeDiskService employeeDiskService;
    
    @Resource(name = "iSysConfigService")
    private ISysConfigService sysConfigService;
    
    @Resource(name = "fileManager")
    private FileManager fileManager;
    
    @Resource(name = "iMessageService")
    private IMessageService messageService;
    
    @Resource(name = "iWorkgroupService")
    private IWorkgroupService  workgroupService;
    
    @Resource(name = "iWorkgroupApplyService")
    private IWorkgroupApplyService workgroupApplyService;
    
    @Resource(name = "iContactService")
    private IContactService  contactService;
    
    /***
     * 登录跳转页面（总览）
     * @param request
     * @param query
     * @return
     * @throws Exception 
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,HttpServletResponse response) throws Exception {
        //跳转拦截 获取配置中的跳转路径
        String index = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.TOLOGINSPAGE.getKey());
        request.getSession().setAttribute("identity", this.generateIdentity(request));
        request.getSession().setAttribute("token", this.generateToken(request));
        String toPath = null; //默认路径总览
        for (SpaceType spaceType : SpaceType.values()) {
            if (spaceType.getType().equals(index)) {
                toPath = spaceType.getPath();
                break;
            }
        }
        String nojump = request.getParameter("nojump");
        if(StringUtil.nullOrBlank(nojump) && !"/home/index".equals(toPath)){
            response.sendRedirect(request.getSession().getServletContext().getContextPath() + toPath);
            return null;
        }
        
        Employee employee = super.getEmployee(request);
        //获取最新云盘基本信息
        EmployeeDisk disk = employeeDiskService.getNewEmployeeDisk(employee.getId());
        employeeDiskService.updateEmployeeDisk(disk);
        disk = employeeDiskService.getEmployeeDiskById(employee.getId());
        //空间大小
        fileManager.spaceInfo(request, employee);
        
        //其他栏
        //未读消息（数量）
        int messageNum = messageService.getUnreadMessageNum(employee.getId());
        //未审核工作组申请（数量）
        WorkgroupApplyQuery workgroupApplyQuery = new WorkgroupApplyQuery();
        workgroupApplyQuery.setWorkgroupEmployeeId(employee.getId());
        workgroupApplyQuery.setPageSize(Integer.MAX_VALUE);
        workgroupApplyQuery.setApplyStatus(WorkgroupApplyStatus.PendingAudit.getStatus());
        int workgroupApplyNum = workgroupApplyService.queryCount(workgroupApplyQuery);
        //联系人
        List<Integer> contactList = contactService.getContactIdByEmployeeId(employee.getId());
        //已加入工作组
        List<Integer> workgroupList = workgroupService.getJoinWorkgroup(employee.getId());
        
        request.setAttribute("messageNum", messageNum);
        request.setAttribute("workgroupApplyNum", workgroupApplyNum);
        request.setAttribute("contactNum", contactList == null ? 0 : contactList.size());
        request.setAttribute("workgroupNum", workgroupList == null ? 0 : workgroupList.size());
        request.setAttribute("admin", employee);
        request.setAttribute("disk", disk);
        return "home/index";
    }
    
}
