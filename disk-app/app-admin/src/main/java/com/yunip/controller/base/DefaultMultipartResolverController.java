package com.yunip.controller.base;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
@RequestMapping("/defaultmultipartresolver")
public class DefaultMultipartResolverController extends CommonsMultipartResolver {
    @Override
    public boolean isMultipart(HttpServletRequest request) {
    	//如果是kindediter 上传的图片，则不走spring的CommonsMultipartResolver
        if (request.getRequestURI().contains("notice/uploadimage")) {
            return false;
        }
        return super.isMultipart(request);
    }
}