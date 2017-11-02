/*
 * 描述：〈对象类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 对象类型
 */
public enum OpenType {

    FOLDER(1,"文件夹"),
    
    FILE(2,"文件");
    
    private int type;
    
    private String desc;
    
    private OpenType(int type, String desc) {
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
