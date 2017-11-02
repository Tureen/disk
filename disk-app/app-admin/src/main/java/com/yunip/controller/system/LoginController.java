package com.yunip.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SysContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.admin.AdminException;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.IsAdminType;
import com.yunip.model.company.Employee;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminAuthority;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IEmployeeService;
import com.yunip.service.authority.IPermissionService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.pwd.Md5;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Resource(name = "iAdminService")
    private IAdminService adminService;
    
    @Resource(name = "iPermissionService")
    private IPermissionService permissionService;
    
    @Resource(name = "iEmployeeService")
    private IEmployeeService employeeService;

    /***
     * 登录跳转页面
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        return "login/index";
    }
    
    /***
     * 退出登录
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/outlogin")
    public String outLogin(HttpServletRequest request) {
        request.getSession().removeAttribute(SysContant.ADMIN_IN_SESSION);
        return "login/index";
    }

    /***
     * 验证帐号密码
     * @param request
     * @param department
     * @return
     * @throws Exception 
     */
    @RequestMapping("/checkLogin")
    @ResponseBody
    public JsonData<?> addDept(HttpServletRequest request, AdminQuery query,
            String validateCode) throws Exception {
        JsonData<?> data = new JsonData<Integer>();
        if (!StringUtils.isNotBlank(query.getMobile())
                || !StringUtils.isNotBlank(query.getPassword())) {
            throw new MyException(AdminException.AORPERROR);
        }
        String checkCode = (String) request.getSession().getAttribute(
                SysContant.CHOCKCODE);
       if (!StringUtils.isNotBlank(validateCode)
                || !validateCode.equalsIgnoreCase(checkCode)) {
            throw new MyException(AdminException.CODEERROR);
//            throw new MyException(AdminException.CODEERROR, query.getMobile());
        }
        query.setPassword(Md5.encodeByMd5(query.getPassword()));
        query.setIsAdmin(IsAdminType.GLY.getType());
        Admin admin = this.adminService.getLoginAdmin(query);
        if (admin == null) {
            throw new MyException(AdminException.AORPERROR);
        }
        //获取员工姓名
        Employee employee = employeeService.getEmployeeById(admin.getId());
        admin.setEmployeeName(employee.getEmployeeName());
        if (admin.getValidStatus().equals(ValidStatus.FREEZE.getStatus())) {
            throw new MyException(AdminException.ACCOUNTFREEZE);
        }
        AdminAuthority adminAuthority = permissionService.getAdAdminAuthority(admin.getId());
        admin.setAdminAuthority(adminAuthority);
        //设置session
        super.setMyinfo(request, admin);
        return data;
    }
}
