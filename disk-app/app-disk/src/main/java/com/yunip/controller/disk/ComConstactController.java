/*
 * 描述：〈公共联系人〉
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
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.IsAdminType;
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.service.IContactService;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.util.EncodingUtil;

@Controller
@RequestMapping("/comcontact")
public class ComConstactController extends BaseController {

    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;

    @Resource(name = "iAdminService")
    private IAdminService    adminService;

    @Resource(name = "iContactService")
    private IContactService  contactService;
    
    /**
     * 部门功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request, EmployeeQuery query) throws Exception {
        query.setAdminValidStatus(ValidStatus.NOMAL.getStatus());
        query.setIsAdmin(IsAdminType.YG.getType());
        if(StringUtils.isNotBlank(query.getEmployeeName())){
            String queryName = URLDecoder.decode(query.getEmployeeName(),SystemContant.encodeType);
            query.setEmployeeName(queryName.trim());
        }
        if(StringUtils.isNotBlank(query.getDeptName())){
            String queryName = URLDecoder.decode(query.getDeptName(),SystemContant.encodeType);
            query.setDeptName(queryName.trim());
        }
        query = employeeService.getEmployeeByQuery(query);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        if(query.getDeptName() != null){
            query.setDeptName(EncodingUtil.decodeUnicodeSwitch(query.getDeptName()));
        }
        request.setAttribute("query", query);
        request.setAttribute("departmentList", departmentList);
        return "comcontact/index";
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> save(HttpServletRequest request,@RequestBody List<String> ids) {
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        contactService.addContacts(employee.getId(),ids);
        return data;
    }

}
