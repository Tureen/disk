package com.yunip.enums.basics;

/**
 * 基础信息布尔值枚举
 */
public enum BasicsBool {

    YES("true","是"),
    
    NO("false","否");

    private BasicsBool(String bool, String desc) {
        this.bool = bool;
        this.desc = desc;
    }

    private String bool;
    
    private String desc;


    public String getBool() {
        return bool;
    }

    public void setBool(String bool) {
        this.bool = bool;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    
    
}
