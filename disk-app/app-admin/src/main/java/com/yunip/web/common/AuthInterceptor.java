/*
 * 描述：〈权限拦截类〉
 * 创建人：can.du
 * 创建时间：2016-4-22
 */
package com.yunip.web.common;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.support.HandlerMethodResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import com.yunip.constant.SysContant;
import com.yunip.enums.admin.AdminException;
import com.yunip.model.user.Admin;
import com.yunip.model.user.AdminAuthority;
import com.yunip.utils.json.JsonUtils;

/**
 * 权限拦截类
 */
public class AuthInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware{
    
    private  ApplicationContext applicationContext ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method method = AnnotationMethodHandlerAdapter.class.getDeclaredMethod("getMethodResolver", Object.class); 
        method.setAccessible(true); 
        HandlerMethodResolver resolver = (HandlerMethodResolver)method.invoke(applicationContext.getBean("annotationMethodHandlerAdapter"), handler);
        Method resolveHandlerMethod = resolver.getClass().getMethod("resolveHandlerMethod", HttpServletRequest.class); 
        resolveHandlerMethod.setAccessible(true); 
        Method executorMethod = (Method)resolveHandlerMethod.invoke(resolver, request);
        Authority authority = executorMethod.getAnnotation(Authority.class);
        if(authority != null){
            //有权限注解 需要获取session中的权限进行验证
            Admin admin = (Admin)request.getSession().getAttribute(SysContant.ADMIN_IN_SESSION);
            if(admin != null){
                AdminAuthority adminAuthority = admin.getAdminAuthority();
                if(adminAuthority != null && adminAuthority.getCodes() != null
                        && adminAuthority.getCodes().contains(authority.value())){
                    return true;
                } else {
                  //跳转分为三种情况(1.页面级跳转，2.ajax load跳转，3.ajax json提醒)
                    String requestType = request.getHeader("X-Requested-With"); 
                    String accept = request.getHeader("Accept"); 
                    if("XMLHttpRequest".equals(requestType) && accept.contains("json")){
                        Map<String, Object> error = new HashMap<String, Object>();
                        error.put("code", AdminException.NOPERMISSION.getCode());
                        error.put("codeInfo", AdminException.NOPERMISSION.getMsg());
                        String errorJson = JsonUtils.getJSONString(error);
                        response.getOutputStream().write(errorJson.getBytes("UTF-8"));  
                        response.setContentType("text/json; charset=UTF-8");  
                        return false; 
                    } else if("XMLHttpRequest".equals(requestType) && !accept.contains("json")){
                        request.getRequestDispatcher("/WEB-INF/view/common/ajaxnopermission.jsp").forward(request,response);
                    } else {
                        //直接跳转登录
                        request.getRequestDispatcher("/WEB-INF/view/common/nopermission.jsp").forward(request,response);
                    }
                }
            }
        }
        return true; 
     }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
}
