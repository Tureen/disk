/*
 * 描述：〈文件及文件夹删除类型〉
 * 创建人：ming.zhu
 * 创建时间：2016-11-17
 */
package com.yunip.enums.disk;

/**
 * 对象类型
 */
public enum DeleteType {

    DIRECT(0,"直接删除"),
    
    INDIRECT(1,"通过上层文件夹删除");
    
    private int type;
    
    private String desc;
    
    private DeleteType(int type, String desc) {
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
