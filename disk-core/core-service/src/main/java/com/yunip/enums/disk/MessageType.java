/*
 * 描述：消息（通知）类型枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-8-16
 */
package com.yunip.enums.disk;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息（通知）类型枚举类
 */
public enum MessageType {
    
    /**
     * 分享消息
     */
    FXXX(1, "分享消息"),
    
    /**
     * 系统消息
     */
    XTXX(2, "系统消息"),
    
    /**
     * 工作组申请
     */
    GZZSQ(3, "工作组申请"),
    
    /**
     * 工作组申请审核
     */
    GZZSH(4, "工作组审核"),
    
    /**
     * 退出工作组
     */
    TCGZZ(5, "退出工作组"),
    
    /**
     * 工作组解散
     */
    GZZJS(6, "工作组解散"),
    
    /**
     * 工作组转让
     */
    GZZZR(7, "工作组转让"),
    
    /**
     * 工作组消息
     */
    GZZXX(8, "工作组消息");
    
    private int code;
    
    private String text;
    
    public static List<MessageType> getMessageTypeList(){
        List<MessageType> list = new ArrayList<MessageType>();
        for(MessageType bean : MessageType.values()){
            list.add(bean);
        }
        return list;
    }
    
    private MessageType(int code, String text){
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
