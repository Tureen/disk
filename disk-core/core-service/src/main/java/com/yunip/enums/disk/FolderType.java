/*
 * 描述：〈文件夹类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 对象类型
 */
public enum FolderType {

    PERSONAL(1,"私人文件夹"),
    
    COMMON(2,"公共文件夹");
    
    private int type;
    
    private String desc;
    
    private FolderType(int type, String desc) {
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
