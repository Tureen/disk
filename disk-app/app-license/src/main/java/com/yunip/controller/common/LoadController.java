package com.yunip.controller.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunip.config.SysConfigHelper;
import com.yunip.constant.JsonData;
import com.yunip.controller.base.BaseController;

@Controller
@RequestMapping("/load")
public class LoadController extends BaseController {

    /****
     * 加载数据
     * @return  
     * JsonData<Object> 
     * @exception
     */
    @RequestMapping("/data")
    @ResponseBody
    public JsonData<Object> index(HttpServletRequest request) {
        JsonData<Object> data = new JsonData<Object>();
        SysConfigHelper.reload();
        return data;
    }
}
