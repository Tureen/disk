/*
 * 描述：〈我的联系人〉
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
import com.yunip.model.company.Department;
import com.yunip.model.company.Employee;
import com.yunip.model.disk.query.ContactQuery;
import com.yunip.service.IContactService;
import com.yunip.service.IDepartmentService;
import com.yunip.utils.util.EncodingUtil;

@Controller
@RequestMapping("/contact")
public class ContactController extends BaseController{
    
    @Resource(name = "iContactService")
    private IContactService  contactService;
    
    /**
     * 部门功能 Service，通过注解实现自动加载
     */
    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;

    @RequestMapping("/index")
    public String index(HttpServletRequest request,ContactQuery query) throws Exception{
        Employee employee = super.getEmployee(request);
        query.setEmployeeId(employee.getId());
        query.setAdminValidStatus(ValidStatus.NOMAL.getStatus());
        if(StringUtils.isNotBlank(query.getEmployeeName())){
            String queryName = URLDecoder.decode(query.getEmployeeName(),SystemContant.encodeType);
            query.setEmployeeName(queryName.trim());
        }
        if(StringUtils.isNotBlank(query.getDeptName())){
            String queryName = URLDecoder.decode(query.getDeptName(),SystemContant.encodeType);
            query.setDeptName(queryName.trim());
        }
        query = contactService.queryContact(query);
        //所有部门获取
        List<Department> departmentList = departmentService.getAllDeparts();
        if(query.getDeptName() != null){
            query.setDeptName(EncodingUtil.decodeUnicodeSwitch(query.getDeptName()));
        }
        request.setAttribute("query", query);
        request.setAttribute("departmentList", departmentList);
        return "contact/index";
    }
    
    @RequestMapping("/delete")
    @ResponseBody
    public JsonData<?> delete(HttpServletRequest request,@RequestBody List<Integer> ids){
        JsonData<?> data = new JsonData<String>();
        Employee employee = super.getEmployee(request);
        contactService.delBatchContact(ids, employee.getId());
        return data;
    }
}
