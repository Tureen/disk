package com.yunip.controller.common;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yunip.constant.SysContant;
import com.yunip.controller.base.BaseController;
import com.yunip.web.helper.ImageCodeHelper;

/*
 *  验证action
 */
@Controller
@RequestMapping("/check")
public class CheckController extends BaseController {
    
    Logger log = Logger.getLogger(this.getClass());

    /**
     * tolifeUserLogin(随机生成生成验证图片)
     * 
     * @return
     * @throws Exception
     *             String
     * @exception
     */
    @RequestMapping("/image")
    public String image(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");
        response.setContentType("image/jpeg");

        try {
            ImageCodeHelper vCode = new ImageCodeHelper(120, 35, 4, 10);
            request.getSession().setAttribute(SysContant.CHOCKCODE, vCode.getCode());
            vCode.write(response.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
