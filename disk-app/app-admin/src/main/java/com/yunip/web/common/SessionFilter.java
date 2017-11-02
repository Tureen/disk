/*
 * 描述：用户权限校验过滤器，针对URL进行权限校验
 * 创建人：junbin.zhou
 * 创建时间：2012-8-23
 */
package com.yunip.web.common;

import java.io.IOException;
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

import com.yunip.constant.SysContant;
import com.yunip.enums.admin.AdminException;
import com.yunip.model.user.Admin;
import com.yunip.utils.json.JsonUtils;

/**
 * 用户权限校验过滤器，针对URL进行权限校验
 */
public class SessionFilter implements Filter {
    
    /**不检查session的路径**/
    String[] noCheckPaths = null;
    
    /**跟路径**/
    String contextPath = null;
    
    public void destroy() {
    }
    
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        String path = request.getServletPath();
        if(path.equals("/")){//访问项目根目录时，调整到登录页面
            response.sendRedirect(contextPath + "/login/index");
            return ;
        }else if(isVerification(path)){
            Admin myinfo = (Admin)request.getSession().getAttribute(SysContant.ADMIN_IN_SESSION);
            if(myinfo == null){
                //跳转分为三种情况(1.页面级跳转，2.ajax load跳转，3.ajax json提醒)
                String requestType = request.getHeader("X-Requested-With"); 
                String accept = request.getHeader("Accept"); 
                if("XMLHttpRequest".equals(requestType) && accept.contains("json")){
                    Map<String, Object> error = new HashMap<String, Object>();
                    error.put("code", AdminException.LOGINFAILURE.getCode());
                    error.put("codeInfo", AdminException.LOGINFAILURE.getMsg());
                    String errorJson = JsonUtils.getJSONString(error);
                    response.getOutputStream().write(errorJson.getBytes("UTF-8"));  
                    response.setContentType("text/json; charset=UTF-8");  
                    return ;
                } else if("XMLHttpRequest".equals(requestType) && !accept.contains("json")){
                    request.getRequestDispatcher("/WEB-INF/view/common/ajaxsessionout.jsp").forward(request,response);
                } else {
                    //直接跳转登录
                    request.getRequestDispatcher("/WEB-INF/view/common/sessionout.jsp").forward(request,response);
                }
            }
        }
        filterChain.doFilter(request, response);
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

    @Override
    public void init(FilterConfig config) throws ServletException {
        //初始化 设置
        contextPath = config.getServletContext().getContextPath();
        //设置不需要检查的路径
        String unCheckPath = config.getInitParameter("unCheckPaths");
        this.noCheckPaths = unCheckPath.split(",");
    }
}
