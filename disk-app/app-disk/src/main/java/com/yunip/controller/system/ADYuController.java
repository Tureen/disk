package com.yunip.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.disk.SpaceType;
import com.yunip.model.company.Employee;
import com.yunip.model.user.Admin;
import com.yunip.service.IEmployeeService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.util.IPUtils;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/adyu")
public class ADYuController extends BaseController{
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;
    
    @Resource(name = "iAdminService")
    private IAdminService adminService;

    /**
     * 跳转ad域注册页面
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toregister")
    public String toRegister(HttpServletRequest request){
        //禁止登录的IP
        String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE, 
                BasicsInfoCode.THROUGHIP.getKey());
        if(StringUtils.isNotBlank(ips)){
            boolean isContain = IPUtils.getConatinsIp(super.getClientIP(request), ips);
            if(isContain){
                toRegisterAction(request);
            }
        } else {
            toRegisterAction(request);
        }
        return "adyu/register";
    }
    
    /***
     * 注册操作
     * @param request  
     * void 
     * @exception
     */
    private void toRegisterAction(HttpServletRequest request){
      //获取配置中的跳转路径
        String index = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.TOLOGINSPAGE.getKey());
        String path = "/home/index"; //默认路径总览
        for (SpaceType spaceType : SpaceType.values()) {
            if (spaceType.getType().equals(index)) {
                path = spaceType.getPath();
                break;
            }
        }
        request.setAttribute("path", path);
    }
    
    /**
     * 域注册
     * @param request
     * @param employee
     * @param admin
     * @return  
     * JsonData<?> 
     * @exception
     */
    @RequestMapping("/register")
    @ResponseBody
    public JsonData<?> register(HttpServletRequest request,Employee employee,Admin admin){
        //限制员工注册数量
        employeeService.checkRegisterLimit(IsAdminType.YG.getType(),null);
        
        JsonData<?> data = new JsonData<String>();
        if(StringUtil.nullOrBlank(employee.getEmployeeName())){
            throw new MyException(DiskException.NOEMPLOYEENAME);
        }
        boolean usernameBoolean = employee.getEmployeeMobile().contains(" ");
        if (usernameBoolean) {
            throw new MyException(DiskException.USERNAMEHAVASPACE);
        }
        boolean passwordBoolean = admin.getAccountPwd().contains(" ");
        if (passwordBoolean) {
            throw new MyException(DiskException.PASSWORDHAVASPACE);
        }
        employeeService.checkEmployeeCode(employee.getEmployeeCode(),null);
        employeeService.checkEmployeeMobile(employee.getEmployeeMobile(),null);
        employeeService.addEmployee(employee, admin);
        super.setEmployee(request, employee);
        return data;
    }
}
