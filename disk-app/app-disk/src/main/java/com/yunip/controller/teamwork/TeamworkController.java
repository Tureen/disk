/*
 * 描述：〈协作信息管理〉
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */
package com.yunip.controller.teamwork;

import java.net.URLDecoder;
import java.util.Iterator;
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
import com.yunip.enums.disk.TeamworkSearchType;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.teamwork.Teamwork;
import com.yunip.model.teamwork.TeamworkEmployee;
import com.yunip.model.teamwork.query.TeamworkQuery;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.ITeamworkEmployeeService;
import com.yunip.service.ITeamworkService;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.util.ListUtil;

@Controller
@RequestMapping("/teamwork")
public class TeamworkController extends BaseController{
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;
    
    @Resource(name = "iTeamworkService")
    private ITeamworkService teamworkService;
    
    @Resource(name = "iTeamworkEmployeeService")
    private ITeamworkEmployeeService teamworkEmployeeService;
    
    /**
     * @throws Exception 
     * 协作浏览页面
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, TeamworkQuery query) throws Exception{
        Employee employee = super.getEmployee(request);
        if(StringUtils.isNotBlank(query.getTeamworkName())){
            String queryName = URLDecoder.decode(query.getTeamworkName(),SystemContant.encodeType);
            query.setTeamworkName(queryName.trim());
        }
        if(query.getTeamworkSearchType() != null && TeamworkSearchType.TEAMWORK_ADMIN.getType() == query.getTeamworkSearchType()){
            query.setEmployeeId(employee.getId());
            query = teamworkService.queryTeamwork(query);
        } else if (query.getTeamworkSearchType() != null && TeamworkSearchType.TEAMWORK_MEMBER.getType() == query.getTeamworkSearchType()){
            query.setTeamworkEmployeeId(employee.getId());
            query = teamworkService.queryTeamwork(query);
            if(query != null && query.getList() != null && query.getList().size() > 0){
                Iterator<Teamwork> iterator = query.getList().iterator();
                while (iterator.hasNext()) {    
                    Teamwork teamwork = iterator.next();    
                    if (teamwork.getEmployeeId() == employee.getId())    
                        iterator.remove();//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException    
                }   
            }
        } else {
            query.setTeamworkEmployeeId(employee.getId());
            query = teamworkService.queryTeamwork(query);
        }
        //员工加入协作
        List<Integer> joinIds = teamworkEmployeeService.getJoinTeamwork(employee.getId());
        //员工拥有协作
        List<Integer> hasIds = teamworkService.getHasTeamwork(employee.getId());
        request.setAttribute("enums", TeamworkSearchType.values());
        request.setAttribute("employeeId", employee.getId());
        request.setAttribute("query", query);
        request.setAttribute("joinIds", joinIds);
        request.setAttribute("hasIds", hasIds);
        return "teamwork/index";
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
        return "teamwork/saveoredit";
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
    public String toedit(HttpServletRequest request, Integer teamworkId){
        Teamwork teamwork = teamworkService.getTeamwork(teamworkId);
        request.setAttribute("teamwork", teamwork);
        return "teamwork/saveoredit";
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
    public JsonData<?> save(HttpServletRequest request, Teamwork teamwork){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        teamworkService.saveOrEdit(teamwork,employee);
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
        teamworkService.delBatchTeamwork(ids, employee);
        return data;
    }
    
    /**
     * 退出协作
     * @param request
     * @param workgroupIds
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/quitteamwork")
    @ResponseBody
    public JsonData<?> quitTeamwork(HttpServletRequest request, @RequestBody List<String> teamworkIds){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        //员工拥有协作
        List<Integer> hasIds = teamworkService.getHasTeamwork(employee.getId());
        List<Integer> lteamworkIds = ListUtil.strToInt(teamworkIds);
        hasIds.retainAll(lteamworkIds);
        if(hasIds.size() > 0){
            throw new MyException(DiskException.TEAMWORK_CREATEADMIN_SAME);
        }
        teamworkEmployeeService.quitBatchTeamwork(employee, teamworkIds);
        return data;
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
    public String loadUser(HttpServletRequest request, Integer teamworkId){
        List<TeamworkEmployee> employeeAddObjs = teamworkEmployeeService.getTeamworkEmployeeIds(teamworkId);
        request.setAttribute("teamworkId", teamworkId);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("employeeAddObjs", JsonUtils.getJSONString(employeeAddObjs).replaceAll("\"", "'"));
        request.setAttribute("departmentList", departmentList);
        return "teamwork/loaduser";
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
    public JsonData<?> saveLoadUser(HttpServletRequest request, Integer teamworkId, @RequestBody List<String> ids){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        teamworkEmployeeService.saveBatchTeamworkEmployee(teamworkId, ids, employee);
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
    public String seeUser(HttpServletRequest request, Integer teamworkId){
        List<TeamworkEmployee> employeeAddObjs = teamworkEmployeeService.getTeamworkEmployeeIds(teamworkId);
        request.setAttribute("teamworkId", teamworkId);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        request.setAttribute("employeeAddObjs", JsonUtils.getJSONString(employeeAddObjs).replaceAll("\"", "'"));
        request.setAttribute("departmentList", departmentList);
        return "workgroup/seeuser";
    }
    
}
