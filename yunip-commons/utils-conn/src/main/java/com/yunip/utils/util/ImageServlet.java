package com.yunip.utils.util;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 生成验证码图片Servlet
 */
public class ImageServlet extends HttpServlet {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 处理get请求
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        //设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        ValidateCode validateCode = new ValidateCode();
        try {
            //调用输出图片方法
            validateCode.getRandcode(request, response, Integer.parseInt(this.getInitParameter("stringNum")),
                    Integer.parseInt(this.getInitParameter("lineSize")),
                    Integer.parseInt(this.getInitParameter("width")),
                    Integer.parseInt(this.getInitParameter("height")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理POST请求
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }
}
