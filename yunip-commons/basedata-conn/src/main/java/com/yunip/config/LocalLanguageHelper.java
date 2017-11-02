/*
 * 描述：本地化语言设置工具类
 * 创建人：ming.zhu
 * 创建时间：2016-11-22
 */
package com.yunip.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.yunip.enums.I18nLanguageType;
import com.yunip.utils.util.StringUtil;

/**
 * 本地化语言设置工具类
 */
public class LocalLanguageHelper {
    private static Logger logger = Logger.getLogger(LocalLanguageHelper.class);
    
    /** cookie中设置本地语言的key  **/
    public static String LOCAL_LANGUAGE_COOKIE = "localLanguage";
    
    /**
     * 国际化配置文件
     */
    private static String properpath  = "i18n/myproperties";
    
    /**
     * 获取设置的本地化语言
     */
    public static String getLocalLanguage(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (LOCAL_LANGUAGE_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        //来自数据库中的配置的语言
        return SysConfigHelper.getValue("BASICS", "LOCALLANGUAGE");
    }
    
    /**
     * 构建ResourceBundle
     */
    private static ResourceBundle createResourceBundle(HttpServletRequest request){
        String localeStr = getLocalLanguage(request);
        Locale locale = null;
        if(StringUtil.nullOrBlank(localeStr)){
            locale = new Locale(I18nLanguageType.CHINA.getCode());
        }else{
            if(I18nLanguageType.DEFAULT.getCode().equals(localeStr)){
                locale = new Locale(I18nLanguageType.CHINA.getCode());
            }else if(I18nLanguageType.ENGLISH.getCode().equals(localeStr)){
                locale = Locale.US;
            }else{
                locale = new Locale(localeStr);
            }
        }
        return ResourceBundle.getBundle(properpath, locale);
    }
    
    /**
     * 根据国际化配置文件中的key获取对应的值
     * @param key 国际化key
     * @return 返回国际化key对应的值
     */
    public static String getI18nValue(String key, HttpServletRequest request){
        String value = "";
        try {
            ResourceBundle bundle = createResourceBundle(request);
            if(bundle != null){
                value = bundle.getString(key);
            }
        }catch (MissingResourceException e) {
            logger.error("i18n fail: key=" + key + ", Message:" + e.getMessage());
        }
        return value;
    }
}
