package com.yunip.web.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jcifs.smb.NtlmPasswordAuthentication;

import org.apache.log4j.Logger;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.disk.DiskException;
import com.yunip.model.company.Employee;
import com.yunip.model.user.Admin;
import com.yunip.model.user.query.AdminQuery;
import com.yunip.service.IEmployeeService;
import com.yunip.service.user.IAdminService;
import com.yunip.util.SpringContextUtil;
import com.yunip.utils.json.JsonUtils;

public class CheckLoginFilter implements Filter {
    
    /**不检查session的路径**/
    String[] noCheckPaths = null;
    
    /**跟路径**/
    String contextPath = null;
    
    private final Logger logger = Logger.getLogger(CheckLoginFilter.class);

    private AdLoginService loginService = new AdLoginService();

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        NtlmPasswordAuthentication ntlm = null;
        //session不需要检测的路径
        String path = request.getServletPath();
        //先检查是否授权
        try {
            //无需验证的命名控件
            /*if(!path.contains("license") && isVerificationJs(path)){
                DiskException diskException = License.checkLicenseCode();
                if(diskException != null){
                    request.setAttribute("exception", diskException);
                    request.getRequestDispatcher("/WEB-INF/view/license/index.jsp").forward(request,response);
                    return;
                }
            }*/
            if(path.equals("/")){//访问项目根目录时，调整到登录页面
                response.sendRedirect(contextPath + "/login/index");
                return;
            } else {
                String isOpenAd = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENAD.getKey());
                if((BasicsBool.YES.getBool()).equals(isOpenAd) && isVerification(path)){
                    //开启
                    Employee employee = (Employee) request.getSession().getAttribute(SysContant.EMPLOYEE_IN_SESSION);
                    if (employee == null) {
                        //不存在session的情况下
                        boolean loginFlag = false;
                        //进行域登录验证
                        loginFlag = (ntlm = loginService.negotiate(request, response, false)) != null;
                        if (loginFlag) {
                            //登录成功
                            logger.info("域账户：" + ntlm.getUsername() + "已登录!");
                            IAdminService adminService = SpringContextUtil.getBean("iAdminService");
                            AdminQuery adminQuery = new AdminQuery();
                            adminQuery.setAdName(ntlm.getUsername());
                            Admin admin = adminService.getLoginAdmin(adminQuery);
                            //不存在
                            if(admin == null){
                                //创建admin -->
                                admin = new Admin();
                                admin.setAdName(ntlm.getUsername());
                                adminService.saveOrUpdate(admin);
                                request.setAttribute("admin", admin);
                                //跳转完善员工页面
                                request.getRequestDispatcher("/WEB-INF/view/adyu/register.jsp").forward(request,response);
                            } else {
                                IEmployeeService employeeService = SpringContextUtil.getBean("iEmployeeService");
                                employee = employeeService.getEmployeeById(admin.getId());
                                if(employee == null){
                                    //跳转完善员工页面
                                    request.setAttribute("admin", admin);
                                    request.getRequestDispatcher("/WEB-INF/view/adyu/register.jsp").forward(request,response);
                                } else {
                                    checkNameSpace(path, request);
                                    request.getSession().setAttribute(SysContant.EMPLOYEE_IN_SESSION, employee);
                                }
                            }
                        }else {
                            //登录失败
                            return;
                        }
                    }
                    checkNameSpace(path, request);
                    chain.doFilter(new NtlmHttpServletRequest(request, ntlm),response);
                } else {
                    if(isVerification(path)){
                        //跳转分为三种情况(1.页面级跳转，2.ajax load跳转，3.ajax json提醒)
                        checkNameSpace(path, request);
                        Employee employee = (Employee)request.getSession().getAttribute(SysContant.EMPLOYEE_IN_SESSION);
                        //跳转分为三种情况(1.页面级跳转，2.ajax load跳转，3.ajax json提醒)
                        String requestType = request.getHeader("X-Requested-With"); 
                        String accept = request.getHeader("Accept"); 
                        if(employee == null){
                            if("XMLHttpRequest".equals(requestType) && accept.contains("json")){
                                Map<String, Object> error = new HashMap<String, Object>();
                                error.put("code", DiskException.LOGINFAILURE.getCode());
                                error.put("codeInfo", DiskException.LOGINFAILURE.getMsg());
                                String errorJson = JsonUtils.getJSONString(error);
                                response.getOutputStream().write(errorJson.getBytes("UTF-8")); 
                                response.setContentType("text/json; charset=UTF-8");  
                            } else if("XMLHttpRequest".equals(requestType) && !accept.contains("json")){
                                request.getRequestDispatcher("/WEB-INF/view/common/ajaxsessionout.jsp").forward(request,response);
                            } else {
                                //直接跳转登录
                                //request.getRequestDispatcher("/WEB-INF/view/common/sessionout.jsp").forward(request,response);
                                response.sendRedirect(contextPath + "/login/index");
                            }
                            return ;
                        }else{
                            String isSingleLogin = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.SINGLELOGIN.getKey());
                            if((BasicsBool.YES.getBool()).equals(isSingleLogin)){//处理同一用户只能在一处进行登录
                                String currentUserId = OnlineUserService.getOnlineUserBySessionId(request.getSession().getId());
                                if(currentUserId == null){
                                    if("XMLHttpRequest".equals(requestType) && accept.contains("json")){
                                        Map<String, Object> error = new HashMap<String, Object>();
                                        error.put("code", DiskException.OFFLINENOTIFICATION.getCode());
                                        error.put("codeInfo", DiskException.OFFLINENOTIFICATION.getMsg());
                                        String errorJson = JsonUtils.getJSONString(error);
                                        response.getOutputStream().write(errorJson.getBytes("UTF-8"));  
                                        response.setContentType("text/json; charset=UTF-8");
                                    }else{
                                        response.sendRedirect(contextPath + "/login/singleOutLogin");
                                    }
                                    return;
                                }
                            }
                        } 
                    } 
                   
                }
            } 
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(FilterConfig config) throws ServletException {
        //初始化 设置
        contextPath = config.getServletContext().getContextPath();
        //设置不需要检查的路径
        String unCheckPath = config.getInitParameter("unCheckPaths");
        this.noCheckPaths = unCheckPath.split(",");
        //初始化 JCIFS的配置信息
        loginService.initJcifsConfig();
    }

    /**
     * isVerification(判断指定的url是否需要验证，默认是需要验证的) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param path
     * @return  false代表不用验证  true 代表需要进行验证
     * Boolean 
     * @exception
     */
    private Boolean isVerification(String path){        
        //默认需要验证url
        boolean isVerification = true;
        for (String p : noCheckPaths) {
            if (path.indexOf(p) == 0) {
                isVerification = false;
                break;
            }
        }
        //view包下的文件不需要验证 
        if (path.indexOf("/view/") == 0 && (path.indexOf(".js") > 0 || path.indexOf(".js?") > 0 )) {
            isVerification = false;
        }
        return isVerification;
    }
    
    /**
     * isVerification(判断指定的url是否需要验证，默认是需要验证的) 
     * @param path
     * @return  false代表不用验证  true 代表需要进行验证
     * Boolean 
     * @exception
     */
    private Boolean isVerificationJs(String path){        
        //默认需要验证url
        boolean isVerification = true;
        //view包下的文件不需要验证 
        if (path.contains("/static") || path.contains("/plugins")) {
            isVerification = false;
        }
        return isVerification;
    }
    

    public void checkNameSpace(String path, HttpServletRequest request){
        String[] namespaces = SysContant.NAME_SPACES;
        for(String namespace : namespaces){
            if(path.contains(namespace)){
                request.setAttribute("namespace", namespace);
                break;
            }
        }
        
    }
    
    @Override
    public void destroy() {
    }

}
