package com.yunip.enums.basics;

public enum LanguageType {

    DEFAULT("local","默认"),
    CHINA("zh_CN", "简体中文"), 
    ENGLISH("en_US", "美国英语");
    
    private String code;

    private String desc;

    private LanguageType(String code, String desc) {
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
