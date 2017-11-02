/*
 * 描述：文件管理控制器层
 * 创建人：jian.xiong
 * 创建时间：2016-5-4
 */
package com.yunip.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.constant.SysContant;
import com.yunip.controller.base.BaseController;
import com.yunip.model.user.Admin;
import com.yunip.utils.pwd.Des;
import com.yunip.utils.pwd.Md5;
import com.yunip.web.common.Authority;

/**
 * 文件管理控制器层
 */
@Controller
@RequestMapping("/fileManager")
public class FileManagerController extends BaseController{
    /**
     * 列表
     */
    @Authority("P20")
    @RequestMapping("/list")
    public String list(HttpServletRequest request, ModelMap modelMap){
        Admin admin = this.getMyinfo(request);
        if(admin != null){
            try {
                Des desObj = new Des();
                String identity = desObj.strEnc(admin.getMobile(), SysContant.desKey, null, null);
                modelMap.put("identity", identity);
                modelMap.put("token", Md5.encodeByMd5(identity + SysContant.md5Key));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "fileManager/list";
    }
}
