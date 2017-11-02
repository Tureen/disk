/**
 * 
 */
package com.yunip.constant;

/**
 * @author can.du
 * 消息发送状态枚举
 */
public enum MsgStatus {
    
    SUCCESS(1,"成功"),
    
    FAIL(0,"失败");

    private int status;
    
    private String desc;

    private MsgStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
