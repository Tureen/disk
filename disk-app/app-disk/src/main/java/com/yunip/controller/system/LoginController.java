package com.yunip.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.ValidStatus;
import com.yunip.enums.company.IsAdminType;
import com.yunip.enums.disk.DiskException;
import com.yunip.enums.log.ActionType;
import com.yunip.manage.LogManager;
import com.yunip.model.company.Employee;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminRole;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IDepartmentService;
import com.yunip.service.IEmployeeService;
import com.yunip.service.authority.IPermissionService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.IPUtils;
import com.yunip.utils.util.StringUtil;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Resource(name = "iAdminService")
    private IAdminService      adminService;

    @Resource(name = "iPermissionService")
    private IPermissionService permissionService;

    @Resource(name = "iEmployeeService")
    private IEmployeeService   employeeService;

    @Resource(name = "iDepartmentService")
    private IDepartmentService departmentService;

    @Resource(name = "logManager")
    private LogManager         logManager;

    /***
     * 登录跳转页面
     * @param request
     * @param query
     * @return
     * @throws Exception 
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().setAttribute("fileServiceUrl",
                SystemContant.FILE_SERVICE_URL);
        String ips = null;
        //查询ip拦截模式
        String ipmode = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.IPMODE.getKey());
        if ((BasicsInfoCode.NOTHROUGHIP.getKey()).equals(ipmode)) {
            //禁止登录的IP
            ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.NOTHROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && !isContain) {
                return "login/index";
            }
        }
        else if ((BasicsInfoCode.NOTHROUGHIP.getKey()).equals(ipmode)) {
            //允许登录的IP
            ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.THROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && isContain) {
                return "login/index";
            }
        }
        Map<String, String> dataMap = super.getLoginCookie(request);
        Employee employee = super.getEmployee(request);
        if (employee != null) {
            return "redirect:/home/index";
        }
        if (isPCClientRequest(request)) {//判断是否来自PC客户端程序的请求
            return "pclogin/index";
        }
        //免登录判断
        if (dataMap != null) {
            //进行登录验证
            AdminQuery query = new AdminQuery();
            query.setMobile(dataMap.get("mobile").trim());
            query.setPassword(dataMap.get("password"));
            query.setIsAdmin(IsAdminType.YG.getType());
            Admin admin = this.adminService.getLoginAdmin(query);
            if (admin == null) {
                super.removeLoginCookie(request, response);
                return "redirect:/login/index";
            }
            //获取员工姓名
            employee = employeeService.getEmployeeById(admin.getId());
            if (admin.getValidStatus().equals(ValidStatus.FREEZE.getStatus())) {
                return "redirect:/login/index";
            }
            operLoginData(query, request, employee);
            return "redirect:/home/index";
        }
        if (isPCClientRequest(request)) {//判断是否来自PC客户端程序的请求
            return "pclogin/index";
        } else {
            return "login/index";
        }
    }

    /***
     * 退出登录
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/loginOut")
    public String outLogin(HttpServletRequest request,
            HttpServletResponse response) {
        super.removeEmployee(request);
        super.removeLoginCookie(request, response);
        request.setAttribute("fileServiceUrl", SystemContant.FILE_SERVICE_URL);
        if (isPCClientRequest(request)) {//判断是否来自PC客户端程序的请求
            request.setAttribute("isPcLoginOut", true);//标识在退出登录时给客户端发送消息
            return "pclogin/index";
        } else {
            return "login/index";
        }
    }

    /***
     * 同一帐号多处登录被退出
     * @return
     */
    @RequestMapping("/singleOutLogin")
    public String singleOutLogin(HttpServletRequest request,
            HttpServletResponse response) {
        super.removeEmployee(request);
        super.removeLoginCookie(request, response);
        return "login/singleOutLogin";
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
    public JsonData<Object> checkLogin(HttpServletRequest request,
            HttpServletResponse response, AdminQuery query, String validateCode)
            throws Exception {
        JsonData<Object> data = new JsonData<Object>();
        //查询ip拦截模式
        String ipmode = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.IPMODE.getKey());
        if ((BasicsInfoCode.NOTHROUGHIP.getKey()).equals(ipmode)) {
            //禁止登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.NOTHROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && !isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        }
        else if ((BasicsInfoCode.THROUGHIP.getKey()).equals(ipmode)) {
            //允许登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.THROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        }
        //设置用户尝试登录标识
        Object attemptlogin = request.getSession().getAttribute(
                SysContant.ATTEMPTLOGIN);
        if (attemptlogin == null) {
            request.getSession().setAttribute(SysContant.ATTEMPTLOGIN, "1");
        }
        query.setMobile(query.getMobile().trim());
        boolean usernameBoolean = query.getMobile().contains(" ");
        if (usernameBoolean) {
            throw new MyException(DiskException.USERNAMEHAVASPACE);
        }
        boolean passwordBoolean = query.getPassword().contains(" ");
        if (passwordBoolean) {
            throw new MyException(DiskException.PASSWORDHAVASPACE);
        }
        if (!StringUtils.isNotBlank(query.getMobile())
                || !StringUtils.isNotBlank(query.getPassword())) {
            throw new MyException(DiskException.AORPERROR);
        }
        if (attemptlogin != null) { //如果有,表示用户已尝试登录过一次
            String checkCode = (String) request.getSession().getAttribute(
                    SysContant.CHOCKCODE);
            if (!StringUtils.isNotBlank(validateCode)
                    || !validateCode.equalsIgnoreCase(checkCode)) {
                throw new MyException(DiskException.CODEERROR);
            }
        }
        query.setPassword(Md5.encodeByMd5(query.getPassword()));
        //检测是否记住账户
        if (query.isBool()) {
            //记住放入cookie（判断是否是来自客户端登录，是客户端登录则设置到客户端的cookie中，不是则设置到其他的cookie中）
            if (isPCClientRequest(request)) {//判断是否来自PC客户端程序的请求
                super.setPCClientCookie(query.getMobile(), query.getPassword(),
                        response);
            } else {
                super.setLoginCookie(query.getMobile(), query.getPassword(),
                        response);
            }
        }
        query.setIsAdmin(IsAdminType.YG.getType());
        Admin admin = this.adminService.getLoginAdmin(query);
        if (admin == null) {
            throw new MyException(DiskException.AORPERROR);
        }
        //获取员工姓名
        Employee employee = employeeService.getEmployeeById(admin.getId());
        if (admin.getValidStatus().equals(ValidStatus.FREEZE.getStatus())) {
            throw new MyException(DiskException.ACCOUNTFREEZE);
        }
        operLoginData(query, request, employee);
        Map<String, String> dataMap = new HashMap<String, String>();
        /*dataMap.put("identity", this.generateIdentity(request));
        dataMap.put("token", this.generateToken(request));*/
        data.setResult(dataMap);
        return data;
    }
    
    /***
     * 验证二维码
     * @param request
     * @param department
     * @return
     * @throws Exception 
     */
    @RequestMapping("/checkLoginQrcode")
    @ResponseBody
    public JsonData<Object> checkLoginQrcode(HttpServletRequest request) throws Exception {
        JsonData<Object> data = new JsonData<Object>();
        //查询ip拦截模式
        String ipmode = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.IPMODE.getKey());
        if ((BasicsInfoCode.NOTHROUGHIP.getKey()).equals(ipmode)) {
            //禁止登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.NOTHROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && !isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        }
        else if ((BasicsInfoCode.THROUGHIP.getKey()).equals(ipmode)) {
            //允许登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.THROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        }
        //判断二维码登录
        String token = request.getParameter("token");
        if (!StringUtil.nullOrBlank(token)) {
            //注入session
            Employee qrcodeEmployee = employeeService.getEmployee(token, null);
            operLoginData(null, request, qrcodeEmployee);
        }
        Map<String, String> dataMap = new HashMap<String, String>();
        data.setResult(dataMap);
        return data;
    }

    /**
     * 处理登录数据
     * @param query  
     * @param employee
     * void 
     * @exception
     */
    public void operLoginData(AdminQuery query, HttpServletRequest request,
            Employee employee) {
        //获取角色
        List<AdminRole> adminRoles = adminService.getAdminRole(employee.getId());
        for (AdminRole adminRole : adminRoles) {
            if (adminRole.getRoleId() == SystemContant.COMMON_MAGENAGE_ID) {
                employee.setCommonShareStatus(true);
                //获取模拟公共空间用户，存入session  
                Employee robotEmployee = employeeService.getEmployeeById(SysContant.COMMON_MANAGE_EMPLOYEE_ID);
                super.setRobotEmployee(request, robotEmployee);
                break;
            }
        }
        //设置session
        employee.setLoginIp(super.getClientIP(request));
        logManager.insertEmpLoyeeLog(ActionType.LOGIN, employee);
        super.setEmployee(request, employee);
    }

    /***
     * PC客户端使用token进行登录
     */
    @RequestMapping("/pcClientLogin")
    public String pcClientLogin(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String fileServiceUrl = (String) request.getSession().getAttribute(
                "fileServiceUrl");
        if (fileServiceUrl == null
                || !fileServiceUrl.equals(SystemContant.FILE_SERVICE_URL)) {
            request.getSession().setAttribute("fileServiceUrl",
                    SystemContant.FILE_SERVICE_URL);
        }
        if (!this.isPCClientRequest(request)) {//判断是否来自PC客户端程序的请求
            throw new MyException(DiskException.PARAMERROR);
        }

        String pcToken = request.getParameter("token");
        if (StringUtil.nullOrBlank(pcToken)) {
            throw new MyException(DiskException.PARAMERROR);
        }

        //查询ip拦截模式
        String ipmode = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.IPMODE.getKey());
        if ((BasicsInfoCode.NOTHROUGHIP.getKey()).equals(ipmode)) {
            //禁止登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.NOTHROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && !isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        } else if ((BasicsInfoCode.THROUGHIP.getKey()).equals(ipmode)) {
            //允许登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.THROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        }

        AdminQuery query = new AdminQuery();
        query.setPcToken(pcToken);
        query.setIsAdmin(IsAdminType.YG.getType());
        Admin admin = this.adminService.getLoginAdmin(query);
        if (admin == null) {
            throw new MyException(DiskException.AORPERROR);
        }
        //获取员工姓名
        Employee employee = employeeService.getEmployeeById(admin.getId());
        if (admin.getValidStatus().equals(ValidStatus.FREEZE.getStatus())) {
            throw new MyException(DiskException.ACCOUNTFREEZE);
        }
        operLoginData(query, request, employee);
        return "redirect:/home/index";
    }

    /***
     * 根据记住的密码登录
     */
    @RequestMapping("/keepPasswordLogin")
    @ResponseBody
    public JsonData<Object> keepPasswordLogin(HttpServletRequest request,
            HttpServletResponse response, AdminQuery query, String validateCode)
            throws Exception {
        JsonData<Object> data = new JsonData<Object>();

        //记住密码登录方式，密码都是从cookie中读取
        Map<String, String> cookieDataMap = super.getPCClientCookie(request);
        if (cookieDataMap != null) {
            query.setPassword(cookieDataMap.get("password"));
        }

        //查询ip拦截模式
        String ipmode = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                BasicsInfoCode.IPMODE.getKey());
        if ((BasicsInfoCode.NOTHROUGHIP.getKey()).equals(ipmode)) {
            //禁止登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.NOTHROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && !isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        } else if ((BasicsInfoCode.THROUGHIP.getKey()).equals(ipmode)) {
            //允许登录的IP
            String ips = SysConfigHelper.getValue(SystemContant.BASICSCODE,
                    BasicsInfoCode.THROUGHIP.getKey());
            boolean isContain = IPUtils.getConatinsIp(
                    super.getClientIP(request), ips);
            if (!StringUtil.nullOrBlank(ips) && isContain) {
                throw new MyException(DiskException.NOTLOGIN);
            }
        }
        //设置用户尝试登录标识
        Object attemptlogin = request.getSession().getAttribute(
                SysContant.ATTEMPTLOGIN);
        if (attemptlogin == null) {
            request.getSession().setAttribute(SysContant.ATTEMPTLOGIN, "1");
        }
        query.setMobile(query.getMobile().trim());
        boolean usernameBoolean = query.getMobile().contains(" ");
        if (usernameBoolean) {
            throw new MyException(DiskException.USERNAMEHAVASPACE);
        }
        boolean passwordBoolean = query.getPassword().contains(" ");
        if (passwordBoolean) {
            throw new MyException(DiskException.PASSWORDHAVASPACE);
        }
        if (!StringUtils.isNotBlank(query.getMobile())
                || !StringUtils.isNotBlank(query.getPassword())) {
            throw new MyException(DiskException.AORPERROR);
        }
        if (attemptlogin != null) { //如果有,表示用户已尝试登录过一次
            String checkCode = (String) request.getSession().getAttribute(
                    SysContant.CHOCKCODE);
            if (!StringUtils.isNotBlank(validateCode)
                    || !validateCode.equalsIgnoreCase(checkCode)) {
                throw new MyException(DiskException.CODEERROR);
            }
        }
        query.setPassword(query.getPassword());
        //检测是否记住账户
        if (query.isBool()) {
            //记住放入cookie
            super.setPCClientCookie(query.getMobile(), query.getPassword(),
                    response);
        } else {
            super.removePCClientCookie(request, response);
        }
        query.setIsAdmin(IsAdminType.YG.getType());
        Admin admin = this.adminService.getLoginAdmin(query);
        if (admin == null) {
            throw new MyException(DiskException.AORPERROR);
        }
        //获取员工姓名
        Employee employee = employeeService.getEmployeeById(admin.getId());
        if (admin.getValidStatus().equals(ValidStatus.FREEZE.getStatus())) {
            throw new MyException(DiskException.ACCOUNTFREEZE);
        }
        operLoginData(query, request, employee);
        return data;
    }

    @RequestMapping("/test")
    public String test(HttpServletRequest request) {
        return "/login/test";
    }

    @RequestMapping("/languagecookie")
    @ResponseBody
    public JsonData<?> languageCookie(HttpServletRequest request,
            HttpServletResponse response, String cookievar) {
        JsonData<?> data = new JsonData<String>();
        setLanguageCookie(cookievar, response);
        return data;
    }

}
