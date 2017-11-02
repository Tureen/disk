/*
 * 描述：国际化配置工具类
 * 创建人：jian.xiong
 * 创建时间：2016-11-14
 */
package com.yunip.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.yunip.enums.I18nLanguageType;
import com.yunip.utils.util.StringUtil;

/**
 * 国际化配置工具类
 */
public class I18nResourceHelper {
    private static Logger logger = Logger.getLogger(I18nResourceHelper.class);
    
    /**
     * 国际化配置文件
     */
    private static String properpath  = "i18n/myproperties";
    
    /**
     * 加载指定的Locale加载资源文件 
     */
    private static ResourceBundle bundle;
    
    static{
        reload();
    }
    
    /**
     * 加载国际化配置的资源文件
     */
    public static void reload(){
        //来自数据库中的配置的语言
        String localeStr = SysConfigHelper.getValue("BASICS", "LOCALLANGUAGE");
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
        bundle = ResourceBundle.getBundle(properpath, locale);
    }
    
    /**
     * 根据国际化配置文件中的key获取对应的值
     * @param key 国际化key
     * @return 返回国际化key对应的值
     */
    public static String getI18nValue(String key){
        String value = "";
        try {
            value = bundle.getString(key);
        }catch (MissingResourceException e) {
            logger.error("i18n fail: key=" + key + ", Message:" + e.getMessage());
        }
        return value;
    }
}
