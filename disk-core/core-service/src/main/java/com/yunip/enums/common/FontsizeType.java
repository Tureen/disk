package com.yunip.enums.common;

public enum FontsizeType {
    
    /**自适应**/
    AUTO(0,"自适应");
    
    private int    type;

    private String desc;

    private FontsizeType(int type, String desc) {
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
