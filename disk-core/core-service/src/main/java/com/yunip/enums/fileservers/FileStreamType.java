/*
 * 描述：文件流类型枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-11-28
 */
package com.yunip.enums.fileservers;

public enum FileStreamType {
    
    /**文件**/
    ALL(1,"所有文件"),
    
    /**缩略图文件流**/
    THUMB_IMAGE(2,"缩略图文件流");

    private FileStreamType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;

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