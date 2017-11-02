package com.yunip.controller.common;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.SystemContant;
import com.yunip.controller.base.BaseController;
import com.yunip.service.IFileService;
import com.yunip.service.user.IAdminService;
import com.yunip.utils.util.FileSizeUtil;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Resource(name = "iAdminService")
    private IAdminService adminService;
    
    @Resource(name = "iFileService")
    private IFileService fileService;

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
        String fileSize = fileService.getFileSize();
        String fileSizeNumber = ""+FileSizeUtil.bytesToSize(Long.valueOf(fileSize));
        request.setAttribute("fileSize", fileSize);
        request.setAttribute("fileSizeNumber", fileSizeNumber);
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
