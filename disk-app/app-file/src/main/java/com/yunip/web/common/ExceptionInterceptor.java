package com.yunip.web.common;

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
import com.yunip.enums.fileservers.FileServersException;


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
	    
	    String path = httpservletrequest.getServletPath();
	    if(path.equals("/upload/upLoad")){//不进行自定义异常的处理
	        String message = exception.getMessage();
	        if(exception instanceof MyException){
                MyException exce = (MyException) exception;
                message = LocalLanguageHelper.getI18nValue(exce.getErrorCode().toString(), httpservletrequest);
            }
	        log.error("异常信息：" + message);
	        return null;
	    }else if("XMLHttpRequest".equals(requestType) && accept.contains("json")){
	        mav = new ModelAndView();
	        //服务器运行异常
	        String message = FileServersException.ERROR.getMsg();
	        Integer code = FileServersException.ERROR.getCode();
	        if(exception instanceof MyException){
	            MyException exce = (MyException) exception;
	            code = exce.getErrorCode();
	            message = exce.getMsg();
	        } 
	        message = LocalLanguageHelper.getI18nValue(code.toString(), httpservletrequest);
	        MappingJacksonJsonView view = new MappingJacksonJsonView();
	        Map<String, Object> error = new HashMap<String, Object>();
	        error.put("code", code);
	        error.put("result", null);
	        error.put("codeInfo", message);
	        view.setAttributesMap(error);
	        log.error("ERROR CODE:"+code+",message:"+message);
	        mav.setView(view);
	    } else if("XMLHttpRequest".equals(requestType) && !accept.contains("json")){
	        String message = FileServersException.ERROR.getMsg();
	        Integer code = FileServersException.ERROR.getCode();
            if(exception instanceof MyException){
                MyException exce = (MyException) exception;
                message = exce.getMsg();
                code = exce.getErrorCode();
            } 
            message = LocalLanguageHelper.getI18nValue(code.toString(), httpservletrequest);
            mav = new ModelAndView("common/500");
            mav.addObject("msg", message);
            log.error("message:"+exception.getMessage());
	    } else {
	        String message = FileServersException.ERROR.getMsg();
	        Integer code = FileServersException.ERROR.getCode();
	        if(exception instanceof MyException){
                MyException exce = (MyException) exception;
                message = exce.getMsg();
                code = exce.getErrorCode();
            }
	        message = LocalLanguageHelper.getI18nValue(code.toString(), httpservletrequest);
	        if(path.indexOf("/authorize/toUploadPage") == -1){
                mav = new ModelAndView("common/500");
            }else{//load弹窗提示界面
                mav = new ModelAndView("common/500_load");
            }
	        mav.addObject("msg", message);
	        log.error("message:"+exception.getMessage());
	    }
		return mav;
	}
	
}
