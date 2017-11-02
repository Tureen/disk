/*
 * 描述：Controller 基类，提供公用功能
 * 创建人：junbin.zhou
 * 创建时间：2012-9-21
 */
package com.yunip.controller.base;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yunip.constant.SysContant;
import com.yunip.enums.company.IsAdminType;
import com.yunip.model.log.AdminLog;
import com.yunip.model.user.Admin;

/**
 * Controller 基类，提供公用功能
 */
public class BaseController {
    
    /**
     * 获取AdminLog对象
     * @param request
     * @return  
     * AdminLog 
     * @exception
     */
    public AdminLog getAdminLog(HttpServletRequest request) {
        AdminLog adminLog = new AdminLog();
        Admin admin = getMyinfo(request);
        adminLog.setAdminId(admin.getId());
        adminLog.setOperAdmin(admin.getEmployeeName());
        adminLog.setOperIp(getClientIP(request));
        adminLog.setOperTime(new Date());
        adminLog.setIsAdmin(IsAdminType.GLY.getType());
        return adminLog;
    }
    
    /**
     * getMyinfo(从session 中获取当前登录人信息) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request  
     * void 
     * @exception
     */
    public Admin getMyinfo(HttpServletRequest request) {
        Admin myinfo = (Admin)request.getSession().getAttribute(SysContant.ADMIN_IN_SESSION);
        return myinfo;
    }
    
    /**
     * setMyinfo(当前登录人信息放入session 中) 
     * @param request  
     * void 
     * @exception
     */
    public void setMyinfo(HttpServletRequest request,Admin admin) {
        request.getSession().setAttribute(SysContant.ADMIN_IN_SESSION , admin);
    }
    
    /**
     * setMyinfo(当前登录人信息放入session 中) 
     * @param request  
     * void 
     * @exception
     */
    public void clearMyinfo(HttpServletRequest request) {
        request.getSession().removeAttribute(SysContant.ADMIN_IN_SESSION);
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
            if (ipAddress.equals("127.0.0.1") || ipAddress.endsWith("0:0:0:0:0:0:1")) {
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
    public static Map decode(HttpServletRequest request)throws Exception{
        //获得request的参数Map
        Map requestParams=request.getParameterMap();
        Iterator iterator =requestParams.keySet().iterator();
        Map urlMap=new HashMap();
        while(iterator.hasNext()){
            String key=String.valueOf(iterator.next());
            String[] values=(String[])requestParams.get(key);
            if(values!=null&&values.length>1){
                for(int i=0;i<values.length;i++){
                    if(values[i].indexOf("objId")>0){
                        /**
                         * 如果objId为空 替换objId 为特殊id
                         */
                        values[i]=values[i].replaceAll("\"objId\":\"\"", "\"@@@\":\"\"");
                    }else{
                        values[i]=URLDecoder.decode(values[i],"utf-8");
                    }
                }
            }else{
                if(values.length==1){
                    if(values[0].indexOf("objId")>0){
                        /**
                         * 如果objId为空 替换objId 为特殊id
                         */
                        values[0]=values[0].replaceAll("\"objId\":\"\"", "\"@@@\":\"\"");
                        
                        urlMap.put(key, URLDecoder.decode(values[0],"utf-8"));
                    }else{
                        if(key.equals("objId")&&"".equals(values[0])){
                            key="&&&";
                        }
                        urlMap.put(key, URLDecoder.decode(values[0],"utf-8"));
                    }
                }
            }
        }
        return urlMap;
    }
}
