/*
 * 描述：解决服务端跨域问题的过滤器
 * 创建人：jian.xiong
 * 创建时间：2016-04-28
 */
package com.yunip.web.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 解决服务端跨域问题的过滤器
 */
public class CrossOriginFilter implements Filter {
    /**
     * 是否跨域部署(文件服务器与上传插件分开部署)
     */
    private boolean isCrossDomains = false;
    
    /**
     * 允许跨域访问的域名
     */
    private String allowCrossDomain;
    
    public void destroy() {
        
    }
    
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain arg2) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        
        if(isCrossDomains && allowCrossDomain != null){
            // 指定允许其他域名访问
            response.addHeader("Access-Control-Allow-Origin", allowCrossDomain);
            // 响应类型  响应方法
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS"); 
            // 响应头设置 
            response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept,X-Requested-With");
            // 缓存此次请求的秒数
            response.addHeader("Access-Control-Max-Age", "3600");
        }
        arg2.doFilter(request, response);
    }
    
	/**
	 * 加载初始化参数
	 */
    public void init(FilterConfig arg0) throws ServletException {
        allowCrossDomain = arg0.getInitParameter("allowCrossDomains");
        isCrossDomains = Boolean.parseBoolean(arg0.getInitParameter("isCrossDomains"));
        if(isCrossDomains && allowCrossDomain == null){
            throw new ServletException("未配置允许跨域域名");
        }
    }
}
