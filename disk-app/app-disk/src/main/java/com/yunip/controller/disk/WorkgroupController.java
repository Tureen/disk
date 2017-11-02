/*
 * 描述：〈工作组管理〉
 * 创建人：ming.zhu
 * 创建时间：2017-1-20
 */
package com.yunip.controller.disk;

import java.net.URLDecoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.WorkgroupApplyStatus;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.disk.Workgroup;
import com.yunip.model.disk.WorkgroupEmployee;
import com.yunip.model.disk.query.WorkgroupApplyQuery;
import com.yunip.model.disk.query.WorkgroupQuery;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.IWorkgroupApplyService;
import com.yunip.service.IWorkgroupService;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.util.ListUtil;

@Controller
@RequestMapping("/workgroup")
public class WorkgroupController extends BaseController{
    
    @Resource(name = "iWorkgroupService")
    private IWorkgroupService  workgroupService;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    @Resource(name = "iWorkgroupApplyService")
    private IWorkgroupApplyService workgroupApplyService;
    
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
    public String index(HttpServletRequest request, WorkgroupQuery query) throws Exception{
        Employee employee = super.getEmployee(request);
        if(StringUtils.isNotBlank(query.getWorkgroupName())){
            String queryName = URLDecoder.decode(query.getWorkgroupName(),SystemContant.encodeType);
            query.setWorkgroupName(queryName.trim());
        }
        query = workgroupService.queryWorkgroup(query);
        //员工加入组
        List<Integer> joinIds = workgroupService.getJoinWorkgroup(employee.getId());
        //员工拥有组
        List<Integer> hasIds = workgroupService.getHasWorkgroup(employee.getId());
        request.setAttribute("employeeId", employee.getId());
        request.setAttribute("query", query);
        request.setAttribute("joinIds", joinIds);
        request.setAttribute("hasIds", hasIds);
        return "workgroup/index";
    }
    
    /**
     * 跳转保存
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/tosave")
    public String tosave(HttpServletRequest request){
        return "workgroup/saveoredit";
    }
    
    /**
     * 跳转修改
     * @param request
     * @param workgroupId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toedit")
    public String toedit(HttpServletRequest request, Integer workgroupId){
        Workgroup workgroup = workgroupService.getWorkgroup(workgroupId);
        request.setAttribute("workgroup", workgroup);
        return "workgroup/saveoredit";
    }
    
    /**
     * 新增or修改
     * @param request
     * @param workgroup
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> save(HttpServletRequest request, Workgroup workgroup){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        workgroupService.saveOrEdit(workgroup,employee);
        return data;
    }
    
    /**
     * 删除
     * @param request
     * @param ids
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/delete")
    @ResponseBody
    public JsonData<?> delete(HttpServletRequest request,@RequestBody List<String> ids){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        workgroupService.delBatchWorkgroup(ids, employee);
        return data;
    }
    
    /**
     * 保存申请人信息至工作组申请表
     * @param request
     * @param workgroupId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/saveapply")
    @ResponseBody
    public JsonData<?> saveApply(HttpServletRequest request, @RequestBody List<String> workgroupIds){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        workgroupApplyService.saveBatchWorkgroupApply(employee, workgroupIds);
        return data;
    }
    
    /**
     * 退出工作组申请
     * @param request
     * @param workgroupIds
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/quitworkgroup")
    @ResponseBody
    public JsonData<?> quitWorkgroup(HttpServletRequest request, @RequestBody List<String> workgroupIds){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        //员工拥有组
        List<Integer> hasIds = workgroupService.getHasWorkgroup(employee.getId());
        List<Integer> lworkgroupIds = ListUtil.strToInt(workgroupIds);
        hasIds.retainAll(lworkgroupIds);
        if(hasIds.size() > 0){
            throw new MyException(DiskException.WORKGROUP_CREATEADMIN_SAME);
        }
        workgroupApplyService.quitBatchWorkgroup(employee, workgroupIds);
        return data;
    }
    
    /**
     * 申请审批
     * @param request
     * @param workgroupId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/applyexamination")
    @ResponseBody
    public JsonData<?> applyExamination (HttpServletRequest request, @RequestBody List<String> ids, Integer workgroupApplyStatus){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        workgroupApplyService.examinationWorkgroupApply(ids, workgroupApplyStatus, employee);
        return data;
    }
    
    /**
     * 读取工作组用户
     * @param request
     * @param workgroupId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/seeuser")
    public String seeUser(HttpServletRequest request, Integer workgroupId){
        List<WorkgroupEmployee> employeeAddObjs = workgroupService.getWorkgroupEmployeeIds(workgroupId);
        request.setAttribute("workgroupId", workgroupId);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("employeeAddObjs", JsonUtils.getJSONString(employeeAddObjs).replaceAll("\"", "'"));
        request.setAttribute("departmentList", departmentList);
        return "workgroup/seeuser";
    }
    
    /**
     * 读取并添加用户
     * @param request
     * @param workgroupId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/loaduser")
    public String loadUser(HttpServletRequest request, Integer workgroupId){
        List<WorkgroupEmployee> employeeAddObjs = workgroupService.getWorkgroupEmployeeIds(workgroupId);
        request.setAttribute("workgroupId", workgroupId);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("employeeAddObjs", JsonUtils.getJSONString(employeeAddObjs).replaceAll("\"", "'"));
        request.setAttribute("departmentList", departmentList);
        return "workgroup/loaduser";
    }
    
    /**
     * ajax获取分页员工信息
     * @param request
     * @param query
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/loaduserajax")
    @ResponseBody
    public JsonData<?> loadUserAjax(HttpServletRequest request, EmployeeQuery query){
        JsonData<EmployeeQuery> data = new JsonData<EmployeeQuery>();
        query.setAdminValidStatus(ValidStatus.NOMAL.getStatus());
        query.setIsAdmin(IsAdminType.YG.getType());
        query = employeeService.getEmployeeByQuery(query);
        data.setResult(query);
        return data;
    }
    
    /**
     * 批量保存用户工作组关联
     * @param request
     * @param ids
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/saveloaduser")
    @ResponseBody
    public JsonData<?> saveLoadUser(HttpServletRequest request, Integer workgroupId, @RequestBody List<String> ids){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        workgroupService.saveBatchWorkgroupEmployee(workgroupId, ids, employee);
        return data;
    }
    
    /**
     * 读取转让用户
     * @param request
     * @param workgroupId
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/seetransfer")
    public String seetransfer(HttpServletRequest request, Integer workgroupId){
        List<WorkgroupEmployee> employeeAddObjs = workgroupService.getWorkgroupEmployeeIds(workgroupId);
        request.setAttribute("workgroupId", workgroupId);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("employeeAddObjs", JsonUtils.getJSONString(employeeAddObjs).replaceAll("\"", "'"));
        request.setAttribute("departmentList", departmentList);
        return "workgroup/seetransfer";
    }
    
    /**
     * 转让
     * @param request
     * @param workgroupId
     * @param employeeId
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/transfer")
    @ResponseBody
    public JsonData<?> transferWorkgroup(HttpServletRequest request, Integer workgroupId, Integer employeeId){
        JsonData<?> data = new JsonData<String>();
        Employee bEmployee = employeeService.getEmployeeById(employeeId);
        Employee employee = super.getEmployee(request);
        workgroupService.transferWorkgroup(workgroupId, employee, bEmployee);
        return data;
    }
    
    /**
     * 我的申请记录
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/myworkgroupapply")
    public String myWorkgroupApply(HttpServletRequest request){
        Employee employee = super.getEmployee(request);
        WorkgroupApplyQuery workgroupApplyQuery = new WorkgroupApplyQuery();
        workgroupApplyQuery.setApplyEmployeeId(employee.getId());
        workgroupApplyQuery = workgroupApplyService.queryWorkgroupApply(workgroupApplyQuery);
        request.setAttribute("query", workgroupApplyQuery);
        request.setAttribute("employee", employee);
        request.setAttribute("enum", WorkgroupApplyStatus.values());
        return "workgroup/seeapply";
    }
}
