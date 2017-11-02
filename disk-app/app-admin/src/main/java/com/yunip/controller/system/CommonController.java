package com.yunip.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.controller.base.BaseController;

@Controller
@RequestMapping("/common")
public class CommonController extends BaseController {


    /***
     * 跳转无权限界面
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/sessionout")
    public String sessionout(HttpServletRequest request) {
        return "common/sessionout";
    }
    
    /***
     * 跳转无权限界面
     * @param request
     * @param query
     * @return
     */
    @RequestMapping("/nopermission")
    public String nopermission(HttpServletRequest request) {
        return "common/nopermission";
    }
}
