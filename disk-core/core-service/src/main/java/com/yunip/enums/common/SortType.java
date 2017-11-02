package com.yunip.enums.common;

/**
 * 排序种类枚举
 */
public enum SortType {
    
    NAMEUP("1","名称升序"),
    
    NAMEDOWN("2","名称降序"),
    
    TIMEUP("3","时间升序"),
    
    TIMEDOWN("4","时间降序"),
    
    SIZEUP("5","大小升序"),
    
    SIZEDOWN("6","大小降序");

    private SortType(String type, String desc) {
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
