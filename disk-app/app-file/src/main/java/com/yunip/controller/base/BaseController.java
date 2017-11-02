/*
 * 描述：Controller 基类，提供公用功能
 * 创建人：junbin.zhou
 * 创建时间：2012-9-21
 */
package com.yunip.controller.base;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.yunip.config.LocalLanguageHelper;
import com.yunip.constant.Constant;
import com.yunip.constant.SystemContant;
import com.yunip.model.UploadUser;
import com.yunip.utils.util.StringUtil;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

/**
 * Controller 基类，提供公用功能
 */
public class BaseController {
	Logger log = Logger.getLogger(this.getClass());
	
	/**
     * getMyinfo(从session 中获取当前登录人信息) 
     */
    public UploadUser getMyInfo(HttpServletRequest request) {
        UploadUser myInfo = (UploadUser)request.getSession().getAttribute(Constant.UPLOADUSER_IN_SESSION);
        return myInfo;
    }
    
    /**
     * 
     * getBean(从request请求中获取参数，封装为数据对象) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param request
     * @param object  
     * void 
     * @exception
     */
    public void getBean(HttpServletRequest request, Object object) {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(object);
        binder.bind(request);
    }
    
    /**
	 * 从请求参数封装 bean
	 * @param request
	 * @param clazz
	 * @return
	 */
	protected <T> T getBean(HttpServletRequest request, Class<T> clazz) {
        T object = null;
        try {
            object = clazz.newInstance();
            getBean(request, object);
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return object;
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
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
           ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * 提供对字符转码输出json字符
     * @param json
     * @throws Exception
     */
    public void write(String json,HttpServletResponse response) throws Exception{
        try {
            response.setContentType("text/html;charset=UTF-8"); 
            PrintWriter out = response.getWriter(); 
            out.print(json.replaceAll("\n", "").replaceAll("\r", "").trim());
            out.flush();                            
            out.close();   
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                	//由于URLDecoder.decode会把加号变成空格，所以这个临时方案
                	values[i] = values[i].replace("+","@@@");
                	values[i]=URLDecoder.decode(values[i],"utf-8").replace("@@@","+");
                }
            }else{
                if(values.length==1){
                	if(!key.equals("companyexpain") && !key.equals("remark") &&  !key.equals("borrowname")){
                		values[0] = values[0].replace("+","@@@");
                		urlMap.put(key, URLDecoder.decode(values[0],"utf-8").replace("@@@","+"));
                	}
                }
            }
        }
        return urlMap;
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
     * 获得当前请求的浏览器版本，判断其是否支持H5
     * 约定使用的IE9及以下版本浏览器不支持，其他浏览器支持
     * @param request
     * @return  
     */
    public boolean getBrowerIsSupportH5(HttpServletRequest request){
        boolean isSupprot = true;
        String userAgentStr = request.getHeader("user-agent");
        if(!StringUtil.nullOrBlank(userAgentStr) && userAgentStr.equals(SystemContant.pcClientUserAgent)){//判断是否来自PC客户端程序的请求
            isSupprot = true;
        }else if(!StringUtil.nullOrBlank(userAgentStr)){
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            String browerType = userAgent.getBrowser().getName();
            Version browserVersion = userAgent.getBrowser().getVersion(userAgentStr);
            if(browerType.indexOf("Internet Explorer") != -1 && Double.parseDouble(browserVersion.getVersion()) < 9){
                isSupprot = false;
            }
        }
        return isSupprot;
    }
    
    /**
     * 获得当前请求的浏览器版本，判断其是否支持文件夹上传
     * 支持文件夹上传的浏览器（Chrome21+, Opera15+）
     * @return true:支持文件夹上传   false:不支持文件夹上传
     */
    public boolean getBrowerIsSupportFolderUpload(HttpServletRequest request){
        boolean isSupprot = false;
        String userAgentStr = request.getHeader("user-agent");
        if(!StringUtil.nullOrBlank(userAgentStr)){
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentStr);
            String browerType = userAgent.getBrowser().getName();
            Version browserVersion = userAgent.getBrowser().getVersion(userAgentStr);
            if((browerType.indexOf(Browser.CHROME.getName()) != -1 && Double.parseDouble(browserVersion.getMajorVersion()) > 21) || (browerType.indexOf(Browser.OPERA.getName()) != -1 && Double.parseDouble(browserVersion.getMajorVersion()) > 15) || (userAgentStr.equals(SystemContant.pcClientUserAgent))){
                isSupprot = true;
            }
        }
        return isSupprot;
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
    
    /***
     * 设置语言类型cookie
     */
    public void setLanguageCookie(String cookievar, HttpServletResponse response) {
        Cookie cookie = new Cookie(LocalLanguageHelper.LOCAL_LANGUAGE_COOKIE, cookievar);
        cookie.setPath("/");
        cookie.setMaxAge(3600 * 24 * 365 * 10);
        response.addCookie(cookie);
    }
    
    /**
     * 获得访问当前项目根目录的请求地址
     * 例如：http://127.0.0.1:8080/file
     * http协议默认80端口 
     */
    public String getRequestRootUrl(HttpServletRequest request){
        //return request.getScheme() + "://" + request.getHeader("host") + request.getContextPath();
        return request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 ? "" : ":" + request.getServerPort()) + request.getContextPath();
    }
}
