package com.yunip.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.constant.JsonData;
import com.yunip.constant.MyException;
import com.yunip.controller.base.BaseController;
import com.yunip.enums.disk.DiskException;
import com.yunip.web.common.License;

@Controller
@RequestMapping("/license")
public class LicenseController extends BaseController{

    /**
     * 跳转输入界面
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/index")
    public String index(HttpServletRequest request){
        return "license/index";
    }
    
    /**
     * @throws Exception 
     * 跳转输入界面
     * @param request
     * @return  
     * String 
     * @exception
     */
    @RequestMapping("/toreg")
    @ResponseBody
    public JsonData<?> toreg(HttpServletRequest request,String serCode) throws Exception{
        JsonData<?> data = new JsonData<Object>();
        try {
            License.generateCode(request,serCode.trim());
        } catch (Exception e) {
           throw new MyException(DiskException.FFSQ);
        }
        return data;
    }
    
}
