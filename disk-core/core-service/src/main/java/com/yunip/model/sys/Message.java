/*
 * 描述：消息（通知）实体类
 * 创建人：jian.xiong
 * 创建时间：2016-8-16
 */
package com.yunip.model.sys;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

/**
 * 消息（通知）实体类
 */
@Alias("TMessage")
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer           id;

    /**
     * 消息类型
     */
    private Integer           msgType;

    /**
     * 消息标题
     */
    private String            title;

    /**
     * 消息内容
     */
    private String            content;

    /**
     * 消息状态
     */
    private Integer           status;

    /**
     * 消息发送时间
     */
    private Date              sendTime;

    /**
     * 发送人ID
     */
    private Integer           senderEmployeeId;

    /**
     * 接收人ID
     */
    private Integer           sendeeEmployeeId;

    /**
     * 阅读消息时间
     */
    private Date              readTime;

    /**
     * 文件id集合
     */
    private String            fileIds;

    /**
     * 文件夹id集合
     */
    private String            folderIds;

    /**
     * 公共关联id
     */
    private Integer           commonId;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
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

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    public String getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(String folderIds) {
        this.folderIds = folderIds;
    }

    public Integer getCommonId() {
        return commonId;
    }

    public void setCommonId(Integer commonId) {
        this.commonId = commonId;
    }
}
