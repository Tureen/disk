/*
 * 描述：〈文件or文件夹删除状态〉
 * 创建人：ming.zhu
 * 创建时间：2016-11-16
 */
package com.yunip.enums.disk;

public enum FileDeleteStatus {
    
    FALSEDELETE(0,"回收站"),
    
    TRUEDELETE(1,"已删除"),
    
    RESTORE(2,"已还原");
    
    private Integer status;
    
    private String desc;
    
    private FileDeleteStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    
}
