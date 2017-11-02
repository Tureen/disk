package com.yunip.controller.jsonp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.admin.AdminType;
import com.yunip.enums.disk.DiskException;
import com.yunip.model.company.Employee;
import com.yunip.model.company.query.EmployeeQuery;
import com.yunip.model.user.Admin;
import com.yunip.service.IEmployeeService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.mail.MailUtil;
import com.yunip.utils.util.StringUtil;

/**
 * 跨域提交
 */
@Controller
@RequestMapping("/jsonp")
public class JsonpController extends BaseController{
    
   @Resource(name = "iEmployeeService")
   private IEmployeeService employeeService;
   
   @Resource(name = "iAdminService")
   private IAdminService adminService;

    @RequestMapping("/register")
    @ResponseBody
    public void register(HttpServletRequest request,HttpServletResponse response,Employee employee) throws Exception{
        /*String ip = super.getClientIP(request);
        if(!(SystemContant.XUANCHUANZHAN_IP).equals(ip)){
            return;
        }*/
        JsonData<Object> jsonData = new JsonData<Object>();
        String mobile = employee.getEmployeeMobile();
        String email = employee.getEmployeeEmail();
        int isAdmin = AdminType.Employee.getType();
        //效验
        for(int i = 0;i<1;i++){
            if (StringUtil.nullOrBlank(mobile)) {
                jsonData.setCode(DiskException.WSRSJ.getCode());
                jsonData.setCodeInfo(DiskException.WSRSJ.getMsg());
                break;
            }
            //手机号格式验证
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
            Matcher m = p.matcher(mobile);
            if(!m.matches()){
                jsonData.setCode(DiskException.SJGSBZQ.getCode());
                jsonData.setCodeInfo(DiskException.SJGSBZQ.getMsg());
                break;
            }
            EmployeeQuery query = new EmployeeQuery();
            query.setEmployeeMobile(mobile);
            query.setIsAdmin(isAdmin);
            Employee configEmployee = employeeService.selectEmpByQuery(query);
            if (configEmployee != null) {
                jsonData.setCode(DiskException.YJCZGSJ.getCode());
                jsonData.setCodeInfo(DiskException.YJCZGSJ.getMsg());
                break;
            }
            //邮箱
            if (StringUtil.nullOrBlank(email)) {
                jsonData.setCode(DiskException.WSRYX.getCode());
                jsonData.setCodeInfo(DiskException.WSRYX.getMsg());
                break;
            }
            //邮箱格式验证
            Pattern pSec = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");  
            Matcher mSec = pSec.matcher(email);
            if(!mSec.matches()){
                jsonData.setCode(DiskException.YXGSBZQ.getCode());
                jsonData.setCodeInfo(DiskException.YXGSBZQ.getMsg());
                break;
            }
            //全部通过
            Admin admin = new Admin();
            admin.setAccountPwd(SystemContant.INITPASSWORD);
            admin.setIsAdmin(isAdmin);
            employeeService.addEmployee(employee, admin);
            jsonData.setCode(1000);
            jsonData.setCodeInfo("申请成功!详细信息已发送至您的邮箱...");
            jsonData.setResult("SUCCESS");
            employee = employeeService.getEmployeeById(employee.getId());
            //给予个人空间权限
            adminService.addAdminPermission(employee.getId());
//            super.setEmployee(request, employee);
            //发送邮件
            sendMail(mobile,email);
        }
       
        String json = JsonUtils.object2json(jsonData);
        this.write("jsonpCallback(" + json + ")", response);
    }
    
    /**
     * 
     * sendMail(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param admin  
     * void 
     * @exception
     */
    private static void sendMail(String username,String email){
        MailUtil mailUtil = new MailUtil();
        mailUtil.iniMail("smtp.exmail.qq.com", email, "xf@mytaoyuan.com", "云盘系统注册成功" ,"xf@mytaoyuan.com", "XUfeng0");
        try {
            mailUtil.sendHtmlMailByQQ("  您的账户是:"+username+" ,初始密码为:123456<br>  请点击以下链接登录: <a href=\"http://103.200.30.130:8082/disk\">云盘管理系统</a>", null, true, false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//        mailUtil.iniMail("smtp.qq.com", email, "469568595@qq.com", "云盘系统注册成功" ,"469568595@qq.com", "citaycnlsbmybihb");
//        try {
//            mailUtil.sendHtmlMailByQQ("  您的账户是:"+username+" ,初始密码为:123456<br>  请点击以下链接:<a href=\"http://192.168.11.72:8090/disk\">云盘管理系统</a>", null, true, false);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    
}
