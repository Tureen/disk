/*
 * 描述：〈资源类型〉
 * 创建人：can.du
 * 创建时间：2016-5-9
 */
package com.yunip.enums.disk;

/**
 * 资源类型
 */
public enum ResourceType {
    
    /**图片**/
    PIC(1,"图片"),
    
    /**文档**/
    FILE(2,"文档"),
    
    /**视频**/
    VIDEO(3,"音频"),
    
    /**其它**/
    OTHER(4,"其它");
   
    private int type;
    
    private String desc;
    
    private ResourceType(int type, String desc) {
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
