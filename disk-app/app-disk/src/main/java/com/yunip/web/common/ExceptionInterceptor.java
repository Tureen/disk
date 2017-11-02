package com.yunip.web.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.yunip.config.LocalLanguageHelper;
import com.yunip.constant.MyException;
import com.yunip.enums.disk.DiskException;


/****
 * 拦截session
 * 
 * @author jonny
 */
public class ExceptionInterceptor implements  HandlerExceptionResolver {

    Logger log = Logger.getLogger(this.getClass());
	
	@Override
	public ModelAndView resolveException(HttpServletRequest httpservletrequest,HttpServletResponse httpservletresponse, Object obj,
			Exception exception) {
	    //请求类型
	    ModelAndView mav = null;
	    String requestType = httpservletrequest.getHeader("X-Requested-With"); 
	    String accept = httpservletrequest.getHeader("Accept"); 
	    if("XMLHttpRequest".equals(requestType) && accept.contains("json")){
	        mav = new ModelAndView();
	        //服务器运行异常
	        String message = DiskException.ERROR.getMsg();
	        Integer code = DiskException.ERROR.getCode();
	        if(exception instanceof MyException){
	            MyException exce = (MyException) exception;
	            code = exce.getErrorCode();
	            message = exce.getMsg();
	        } 
	        MappingJacksonJsonView view = new MappingJacksonJsonView();
	        Map<String, Object> error = new HashMap<String, Object>();
	        error.put("code", code);
	        error.put("result", null);
	        error.put("codeInfo", LocalLanguageHelper.getI18nValue(code.toString(), httpservletrequest));
	        view.setAttributesMap(error);
	        log.error("ERROR CODE:"+code+",message:"+message);
	        mav.setView(view);
	    } else if("XMLHttpRequest".equals(requestType) && !accept.contains("json")){
            mav = new ModelAndView("common/ajax500");
            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            try {
                exception.printStackTrace(new PrintWriter(buf, true));
                log.error("message:"+buf);
                buf.close();
            } catch (IOException e) {
                log.error("message:"+buf);
            }
	    } else {
	        mav = new ModelAndView("common/500");
	        ByteArrayOutputStream buf = new ByteArrayOutputStream();
	        try {
    	        exception.printStackTrace(new PrintWriter(buf, true));
    	        log.error("message:"+buf);
                buf.close();
            } catch (IOException e) {
                log.error("message:"+buf);
            }
	    }
		return mav;
	}
	
}
