package com.yunip.controller.upload;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController{

    /***
     * 登录跳转页面
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/upload")
    public String index(HttpServletRequest request) {
        return "upload/index";
    }

}
