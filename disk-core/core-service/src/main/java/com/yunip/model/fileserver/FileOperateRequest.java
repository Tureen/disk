/*
 * 描述：与文件服务器交互请求实体类
 * 创建人：jian.xiong
 * 创建时间：2016-5-27
 */
package com.yunip.model.fileserver;

import java.io.Serializable;
import java.util.List;

/**
 * 与文件服务器交互请求实体类
 */
public class FileOperateRequest<T> implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 接口ID
     */
    private String cmdId;

    /**
     * 调用接口人ID
     */
    private String operateId;
    
    /**
     * 操作时间(yyyyMMddHH24mmss)
     */
    private String operateTime;
    
    /**
     * 操作数据字符串
     * 用于校验数据有效性
     */
    private String chkValue;
    
    /**
     * 接口参数
     */
    private List<T> params;

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getOperateId() {
        return operateId;
    }

    public void setOperateId(String operateId) {
        this.operateId = operateId;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public String getChkValue() {
        return chkValue;
    }

    public void setChkValue(String chkValue) {
        this.chkValue = chkValue;
    }

    public List<T> getParams() {
        return params;
    }

    public void setParams(List<T> params) {
        this.params = params;
    }
}
