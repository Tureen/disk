package com.yunip.enums;

/**
 * @author ming.zhu
 * 系统代码的枚举
 */
public enum SystemCode {

    /**后台**/
    OTM("OTM", "后台"),

    /**前台**/
    MEM("MEM", "前台");

    private SystemCode(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private String type;

    private String desc;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
