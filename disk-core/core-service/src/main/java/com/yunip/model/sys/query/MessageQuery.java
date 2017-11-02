/*
 * 描述：消息（通知）查询实体类
 * 创建人：jian.xiong
 * 创建时间：2016-8-16
 */
package com.yunip.model.sys.query;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.model.sys.Message;
import com.yunip.utils.page.PageQuery;

/**
 * 消息（通知）查询实体类
 */
@Alias("TMessageQuery")
public class MessageQuery extends PageQuery<Message> implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer id;
    
    /**
     * 消息类型
     */
    private Integer msgType;
    
    /**
     * 消息状态
     */
    private Integer status;
    
    /**
     * 发送人ID
     */
    private Integer senderEmployeeId;
    
    /**
     * 接收人ID
     */
    private Integer sendeeEmployeeId;
    
    /**
     * 主键ID数组
     */
    private List<Integer> ids;
    
    /**
     * 阅读消息时间
     */
    private Date readTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSenderEmployeeId() {
        return senderEmployeeId;
    }

    public void setSenderEmployeeId(Integer senderEmployeeId) {
        this.senderEmployeeId = senderEmployeeId;
    }

    public Integer getSendeeEmployeeId() {
        return sendeeEmployeeId;
    }

    public void setSendeeEmployeeId(Integer sendeeEmployeeId) {
        this.sendeeEmployeeId = sendeeEmployeeId;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}
