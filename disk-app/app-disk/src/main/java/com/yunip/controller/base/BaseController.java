/*
 * 描述：Controller 基类，提供公用功能
 * 创建人：junbin.zhou
 * 创建时间：2012-9-21
 */
package com.yunip.controller.base;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunip.config.LocalLanguageHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.MyException;
import com.yunip.constant.SysContant;
import com.yunip.constant.SystemContant;
import com.yunip.enums.api.ApiException;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.enums.common.IdentityIndex;
import com.yunip.model.company.Employee;
import com.yunip.utils.json.JsonUtils;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.utils.util.StringUtil;
import com.yunip.web.common.OnlineUserService;

/**
 * Controller 基类，提供公用功能
 */
public class BaseController {

    /**
     * 首页列表模式选择
     * @param request
     * @param type
     * @return  
     * IdentityIndex 
     * @exception
     */
    public IdentityIndex checkIndexIdentification(HttpServletRequest request,
            String type) {
        type = type.trim();
        if (type != null && !StringUtil.nullOrBlank(type)
                && type.equals("" + IdentityIndex.ICONINDEX.getType())) {
            setIndexIdentification(request, IdentityIndex.ICONINDEX.getType());
            return IdentityIndex.ICONINDEX;
        }
        else if (type != null && !StringUtil.nullOrBlank(type)
                && type.equals("" + IdentityIndex.TABLEINDEX.getType())) {
            setIndexIdentification(request, IdentityIndex.TABLEINDEX.getType());
            return IdentityIndex.TABLEINDEX;
        }
        else {
            Integer i = getIndexIdentification(request);
            if (i == null || i.equals(IdentityIndex.TABLEINDEX.getType())) {
                return IdentityIndex.TABLEINDEX;
            }
            else {
                return IdentityIndex.ICONINDEX;
            }
        }
    }

    /**
     * 首页展示模式session存入
     * @param request
     * @param type  
     * void 
     * @exception
     */
    public void setIndexIdentification(HttpServletRequest request, Integer type) {
        request.getSession().setAttribute(SysContant.INDEXIDENTIFICATION, type);
    }

    /**
     * 首页展示模式session取出
     * @param request
     * @return  
     * int 
     * @exception
     */
    public Integer getIndexIdentification(HttpServletRequest request) {
        Object object = request.getSession().getAttribute(
                SysContant.INDEXIDENTIFICATION);
        if (object != null) {
            return Integer.valueOf(""
                    + request.getSession().getAttribute(
                            SysContant.INDEXIDENTIFICATION));
        }
        else {
            return null;
        }
    }

    /**
     * getEmployee(从session 中获取当前登录人信息) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request  
     * void 
     * @exception
     */
    public Employee getEmployee(HttpServletRequest request) {
        Employee employee = (Employee) request.getSession().getAttribute(
                SysContant.EMPLOYEE_IN_SESSION);
        return employee;
    }

    /**
     * removeEmployee(从session 中移除当前登录人信息) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request  
     * void 
     * @exception
     */
    public void removeEmployee(HttpServletRequest request) {
        request.getSession().removeAttribute(SysContant.EMPLOYEE_IN_SESSION);
    }

    /**
     * setEmployee(当前登录人信息放入session 中) 
     * @param request  
     * void 
     * @exception
     */
    public void setEmployee(HttpServletRequest request, Employee employee) {
        request.getSession().setAttribute(SysContant.EMPLOYEE_IN_SESSION,
                employee);
        String isSingleLogin = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.SINGLELOGIN.getKey());
        if((BasicsBool.YES.getBool()).equals(isSingleLogin)){//判断是否启用单点登录
            OnlineUserService.addOnlineUser(employee.getId().toString(), request.getSession().getId());
        }
    }

    /**
     * getEmployee(从session 中获取当前模拟人信息) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request  
     * void 
     * @exception
     */
    public Employee getRobotEmployee(HttpServletRequest request) {
        Employee employee = (Employee) request.getSession().getAttribute(
                SysContant.COMMON_MANAGE_EMPLOYEE_SESSION);
        return employee;
    }

    /**
     * removeEmployee(从session 中移除当前模拟人信息) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request  
     * void 
     * @exception
     */
    public void removeRobotEmployee(HttpServletRequest request) {
        request.getSession().removeAttribute(
                SysContant.COMMON_MANAGE_EMPLOYEE_SESSION);
    }

    /**
     * setEmployee(当前模拟人信息放入session 中) 
     * @param request  
     * void 
     * @exception
     */
    public void setRobotEmployee(HttpServletRequest request, Employee employee) {
        request.getSession().setAttribute(
                SysContant.COMMON_MANAGE_EMPLOYEE_SESSION, employee);
    }

    /**
     * 
     * getClientIP(获取客户端IP) 
     * request.getRemoteAddr(),这种方法在大部分情况下都是有效的,
     * 但是在通过了Apache,Squid等反向代理软件就不能获取到客户端的真实IP地址了.
     * @param request
     * @return  
     * String 
     * @exception
     */
    public String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();

            //这里主要是获取本机的ip,可有可无
            if (ipAddress.equals("127.0.0.1")
                    || ipAddress.endsWith("0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        //或者这样也行,对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
        //return ipAddress!=null&&!"".equals(ipAddress)?ipAddress.split(",")[0]:null;       
        return ipAddress;
    }

    /**
     * 将url编码转换
     * decode(这里用一句话描述这个方法的作用) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request
     * @return
     * @throws Exception  
     * Map 
     * @exception
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map decode(HttpServletRequest request) throws Exception {
        //获得request的参数Map
        Map requestParams = request.getParameterMap();
        Iterator iterator = requestParams.keySet().iterator();
        Map urlMap = new HashMap();
        while (iterator.hasNext()) {
            String key = String.valueOf(iterator.next());
            String[] values = (String[]) requestParams.get(key);
            if (values != null && values.length > 1) {
                for (int i = 0; i < values.length; i++) {
                    if (values[i].indexOf("objId") > 0) {
                        /**
                         * 如果objId为空 替换objId 为特殊id
                         */
                        values[i] = values[i].replaceAll("\"objId\":\"\"",
                                "\"@@@\":\"\"");
                    }
                    else {
                        values[i] = URLDecoder.decode(values[i], "utf-8");
                    }
                }
            }
            else {
                if (values.length == 1) {
                    if (values[0].indexOf("objId") > 0) {
                        /**
                         * 如果objId为空 替换objId 为特殊id
                         */
                        values[0] = values[0].replaceAll("\"objId\":\"\"",
                                "\"@@@\":\"\"");

                        urlMap.put(key, URLDecoder.decode(values[0], "utf-8"));
                    }
                    else {
                        if (key.equals("objId") && "".equals(values[0])) {
                            key = "&&&";
                        }
                        urlMap.put(key, URLDecoder.decode(values[0], "utf-8"));
                    }
                }
            }
        }
        return urlMap;
    }

    /***
     * 设置免登录cookie
     * @param mobile
     * @param password
     * @param response  
     * void 
     * @exception
     */
    public void setLoginCookie(String mobile, String password,
            HttpServletResponse response) {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("mobile", mobile);
        dataMap.put("password", password);
        String loginInfo = JsonUtils.getJSONString(dataMap);
        Cookie cookie = new Cookie(SysContant.ADMIN_IN_COOKIE, loginInfo);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24 * 365 * 10);
        response.addCookie(cookie);
    }

    /**
     * 移除免登录cookie
     * @param request
     * @param response  
     * void 
     * @exception
     */
    public void removeLoginCookie(HttpServletRequest request,
            HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (SysContant.ADMIN_IN_COOKIE.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }

    /**
     * 获取免登录cookie
     * @param request
     * @return  
     * Map<String,String> 
     * @exception
     */
    public Map<String, String> getLoginCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (SysContant.ADMIN_IN_COOKIE.equals(cookie.getName())) {
                    return JsonUtils.json2Bean(cookie.getValue(), Map.class);
                }
            }
        }
        return null;
    }
    
    /***
     * 设置语言类型cookie
     * @param mobile
     * @param password
     * @param response  
     * void 
     * @exception
     */
    public void setLanguageCookie(String cookievar, HttpServletResponse response) {
        Cookie cookie = new Cookie(LocalLanguageHelper.LOCAL_LANGUAGE_COOKIE, cookievar);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24 * 365 * 10);
        response.addCookie(cookie);
    }


    /***
     * 设置PC客户端登录时的cookie
     */
    public void setPCClientCookie(String mobile, String password,
            HttpServletResponse response) {
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("mobile", mobile);
        dataMap.put("password", password);
        String loginInfo = JsonUtils.getJSONString(dataMap);
        Cookie cookie = new Cookie(SysContant.ADMIN_PCCLIENT_IN_COOKIE, loginInfo);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24 * 365 * 10);
        response.addCookie(cookie);
    }
    
    /**
     * 移除PC客户端登录时的cookie
     */
    public void removePCClientCookie(HttpServletRequest request,
            HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (SysContant.ADMIN_PCCLIENT_IN_COOKIE.equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
    
    /**
     * 获取PC客户端登录时的cookie
     */
    public Map<String, String> getPCClientCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (SysContant.ADMIN_PCCLIENT_IN_COOKIE.equals(cookie.getName())) {
                    return JsonUtils.json2Bean(cookie.getValue(), Map.class);
                }
            }
        }
        return null;
    }

    /**
     * 生成加密的用户身份信息
     */
    public String generateIdentity(HttpServletRequest request) {
        String identity = "";
        Employee employee = this.getEmployee(request);
        if (employee != null) {
            Des desObj = new Des();
            identity = desObj.strEnc(employee.getId().toString(),
                    SystemContant.desKey, null, null);
        }
        return identity;
    }

    /**
     * 生成token
     */
    public String generateToken(HttpServletRequest request) throws Exception {
        String token = "";
        String identity = this.generateIdentity(request);
        if (!StringUtil.nullOrBlank(identity)) {
            token = Md5.encodeByMd5(identity + SystemContant.md5Key);
        }
        return token;
    }

    //上传和下载
    public void downLoadRequest(HttpServletRequest request, String code) {
        request.setAttribute("fileServiceUrl", SystemContant.FILE_SERVICE_URL);
        try {
            request.setAttribute("identity", this.generateIdentity(request));
            request.setAttribute("token", this.generateToken(request));
            //空间类型
            request.setAttribute("spaceType",
                    Md5.encodeByMd5(code + SystemContant.md5Key));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提供对字符转码输出json字符
     * @param json
     * @throws Exception
     */
    public void write(String json, HttpServletResponse response)
            throws Exception {
        try {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json.replaceAll("\n", "").replaceAll("\r", "").trim());
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * isPCClientRequest 
     * 判断是否来自PC客户端的请求
     * return 是：true  不是：false
     */
    public boolean isPCClientRequest(HttpServletRequest request){
        String userAgent = request.getHeader("User-Agent");
        if(!StringUtil.nullOrBlank(userAgent) && userAgent.equals(SystemContant.pcClientUserAgent)){//判断是否来自PC客户端程序的请求
            return true;
        }
        return false;
    }
    
    /**
     * 读取post报文
     * @param request
     * @return
     */
    public String getPostContent(HttpServletRequest request) {
        try {
            ServletInputStream in = request.getInputStream();  
            StringBuilder sb = new StringBuilder();  
            byte[] b = new byte[4096];  
            for (int n; (n = in.read(b)) != -1;) {  
                sb.append(new String(b, 0, n, "UTF-8"));  
            }  
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * 设置Http接口请求时的cookies值
     */
    public String setHttpRequestCookies(HttpServletRequest request){
        return LocalLanguageHelper.LOCAL_LANGUAGE_COOKIE + "=" + LocalLanguageHelper.getLocalLanguage(request);
    }
    
    /**
     * json多层格式转换
     */
    @SuppressWarnings("rawtypes")
    protected <T extends Object> T getJsonData(String jsonData, Class<T> clazz, Map<String, Class> clazzMap) {
        T t = null;
        try {
            t = JsonUtils.json2Bean(jsonData, clazz, clazzMap);
        } catch (Exception e) {
            throw new MyException(ApiException.ILLEAGEREQUEST);
        }
        //checkSecret(bean);
        return t;
    }
}
