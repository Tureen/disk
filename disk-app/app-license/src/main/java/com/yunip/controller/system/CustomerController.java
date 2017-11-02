package com.yunip.controller.system;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.au.ActionType;
import com.yunip.model.au.Customer;
import com.yunip.model.au.LicenseCode;
import com.yunip.model.au.LicenseLog;
import com.yunip.model.au.query.CustomerQuery;
import com.yunip.model.au.query.LicenseLogQuery;
import com.yunip.model.user.Admin;
import com.yunip.service.ICustomerService;
import com.yunip.service.ILicenseLogService;
import com.yunip.utils.date.DateUtil;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController{
    
    @Resource(name = "iCustomerService")
    private ICustomerService customerService;
    
    @Resource(name = "iLicenseLogService")
    private ILicenseLogService licenseLogService;
    
    /**
     * 客户端首页
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request,CustomerQuery query){
        if(StringUtils.isNotBlank(query.getEndDate())){
            //在结束日期中加上一天
            Date endDateTime = DateUtil.parseDate(query.getEndDate(), DateUtil.YMD_DATA) ;
            query.setEndDate(DateUtil.getDateFormat(DateUtil.getAddDates(endDateTime, 1), DateUtil.YMD_DATA));
        }
        query = customerService.queryCustomer(query);
        request.setAttribute("query", query);
        return "customer/index";
    }
    
    @RequestMapping("/toedit")
    public String toEdit(HttpServletRequest request,Integer id){
        Customer customer = customerService.getCustomer(id);
        request.setAttribute("customer", customer);
        return "customer/edit";
    }
    
    @RequestMapping("/edit")
    @ResponseBody
    public JsonData<?> update(HttpServletRequest request,Customer customer){
        JsonData<?> data = new JsonData<String>();
        LicenseLog licenseLog = new LicenseLog();
        Admin admin = getMyinfo(request);
        licenseLog.setCustomerId(customer.getId());
        licenseLog.setAdminId(admin.getId());
        licenseLog.setOperIp(getClientIP(request));
        licenseLog.setOperAdmin(admin.getEmployeeName());
        licenseLog.setOperTime(new Date());
        customerService.updateCustomer(customer,licenseLog);
        return data;
    }
    
    @RequestMapping("/tosave")
    public String toInsert(HttpServletRequest request){
        return "customer/save";
    }
    
    @RequestMapping("/save")
    @ResponseBody
    public JsonData<?> insert(HttpServletRequest request,Customer customer,LicenseCode licenseCode){
        JsonData<?> data = new JsonData<String>();
        customerService.addCustomer(customer, licenseCode);
        return data;
    }
    
    /**
     * 日志
     * log(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request
     * @param query
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/log")
    public String log(HttpServletRequest request, LicenseLogQuery query){
        query.setPageSize(Integer.MAX_VALUE);
        query = licenseLogService.queryLicenseLog(query);
        request.setAttribute("query", query);
        request.setAttribute("enum", ActionType.values());
        return "customer/log";
    }
    
    
}
