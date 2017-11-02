/*
 * 描述：国际化语言枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-11-14
 */
package com.yunip.enums;

/**
 * 国际化语言枚举类（与LanguageType枚举类一致）
 */
public enum I18nLanguageType {

    DEFAULT("local","默认"),
    CHINA("zh_CN", "简体中文"), 
    ENGLISH("en_US", "美国英语");
    
    private String code;

    private String desc;

    private I18nLanguageType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
