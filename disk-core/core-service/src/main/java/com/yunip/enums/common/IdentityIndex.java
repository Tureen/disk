package com.yunip.enums.common;

/**
 * @author ming.zhu
 * 云盘列表展示模式
 */
public enum IdentityIndex {

    TABLEINDEX(0, "表格列表"),

    ICONINDEX(1, "图形列表");
    
    private int    type;

    private String desc;

    private IdentityIndex(int type, String desc) {
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
