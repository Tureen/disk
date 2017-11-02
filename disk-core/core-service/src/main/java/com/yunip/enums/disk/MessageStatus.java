/*
 * 描述：消息（通知）状态枚举类
 * 创建人：jian.xiong
 * 创建时间：2016-8-16
 */
package com.yunip.enums.disk;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息（通知）状态枚举类
 */
public enum MessageStatus {
    /**
     * 未读
     */
    WD(0, "未读"),
    
    /**
     * 已读
     */
    YD(1, "已读");
    
    private int code;
    
    private String text;
    
    public static List<MessageStatus> getMessageTypeList(){
        List<MessageStatus> list = new ArrayList<MessageStatus>();
        for(MessageStatus bean : MessageStatus.values()){
            list.add(bean);
        }
        return list;
    }
    
    private MessageStatus(int code, String text){
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
