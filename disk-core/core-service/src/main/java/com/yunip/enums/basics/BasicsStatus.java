package com.yunip.enums.basics;

/**
 * 基础信息状态枚举
 */
public enum BasicsStatus {

    ONE("1","第一个选项"),
    
    TWO("2","第二个选项"),
    
    THREE("3","第三个个选项"),
    
    FOUR("4","第四个选项"),
    
    FIVE("5","第五个选项"),
    
    SIX("6","第六个选项"),
    
    SEVEN("7","第七个选项"),
    
    EIGHT("8","第八个选项");

    private BasicsStatus(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    private String status;
    
    private String desc;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    
    
}
