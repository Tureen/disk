package com.yunip.enums.common;

public enum AlignType {
    
    /**居中**/
    TOP(1,"顶部"),
    
    /**居左**/
    CENTER(2,"居中"),
    
    /**居右**/
    BOTTOM(3,"底部");
    
    private int    type;

    private String desc;

    private AlignType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
