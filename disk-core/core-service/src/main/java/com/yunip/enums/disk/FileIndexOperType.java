/*
 * 描述：〈文件索引操作类型〉
 * 创建人：ming.zhu
 * 创建时间：2016-8-5
 */
package com.yunip.enums.disk;

public enum FileIndexOperType {
    
    SAVE(1,"新增"),
    
    DEL(2,"删除"),
    
    UPDATE(3,"更新");
    
    private int    type;

    private String desc;

    private FileIndexOperType(int type, String desc) {
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
