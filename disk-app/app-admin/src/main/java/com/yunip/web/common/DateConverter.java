/*
 * 描述：spring 数据绑定日期格式转换
 * 创建人：junbin.zhou
 * 创建时间：2012-9-7
 */
package com.yunip.web.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * spring 数据绑定日期格式转换
 * 绑定日期格式为 YYYY-MM-DD HH:mm:ss
 * 如果需要使用其他日期格式，请在 Controller 中重写 initBinder 或不使用spring 数据绑定
 */
public class DateConverter implements WebBindingInitializer {
    /* (non-Javadoc) 
     * @see org.springframework.web.bind.support.WebBindingInitializer#initBinder(org.springframework.web.bind.WebDataBinder, org.springframework.web.context.request.WebRequest) 
     */
    public void initBinder(WebDataBinder binder, WebRequest request) {
        // TODO Auto-generated method stub
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, true));
    }
}
