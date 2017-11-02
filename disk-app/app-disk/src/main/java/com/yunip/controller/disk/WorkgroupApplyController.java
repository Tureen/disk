/*
 * 描述：〈工作组申请管理〉
 * 创建人：ming.zhu
 * 创建时间：2017-02-04
 */
package com.yunip.controller.disk;

import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.disk.WorkgroupApplyStatus;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.query.WorkgroupApplyQuery;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.IWorkgroupApplyService;
import com.yunip.service.IWorkgroupService;

@Controller
@RequestMapping("/wgapply")
public class WorkgroupApplyController extends BaseController{
    
    @Resource(name = "iWorkgroupService")
    private IWorkgroupService  workgroupService;
    
    @Resource(name = "iWorkgroupApplyService")
    private IWorkgroupApplyService workgroupApplyService;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    /**
     * @throws Exception 
     * 工作组浏览页面
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, WorkgroupApplyQuery query) throws Exception{
        Employee employee = super.getEmployee(request);
        if(StringUtils.isNotBlank(query.getWorkgroupName())){
            String queryName = URLDecoder.decode(query.getWorkgroupName(),SystemContant.encodeType);
            query.setWorkgroupName(queryName.trim());
        }
        query.setWorkgroupEmployeeId(employee.getId());
        query = workgroupApplyService.queryWorkgroupApply(query);
        String workgroupApplyId = request.getParameter("workgroupApplyId");
        request.setAttribute("workgroupApplyId", workgroupApplyId);
        request.setAttribute("query", query);
        request.setAttribute("employee", employee);
        request.setAttribute("enum", WorkgroupApplyStatus.values());
        return "workgroupapply/index";
    }
}
