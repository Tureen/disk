package com.yunip.controller.common;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.controller.base.BaseController;
import com.yunip.service.user.IAdminService;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Resource(name = "iAdminService")
    private IAdminService adminService;

    /***
     * 登录跳转页面
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request) {
        request.setAttribute("admin", getMyinfo(request));
        return "home/index";
    }

    
    @RequestMapping("/welcome")
    public String welcome(HttpServletRequest request,HttpServletResponse response){
        return "home/welcome";
    }
    
    /**
     * 系统运行环境信息
     */
    @RequestMapping("/systemRunInfo")
    public String systemRunInfo(HttpServletRequest request,HttpServletResponse response){
        return "home/systemRunInfo";
    }
}
