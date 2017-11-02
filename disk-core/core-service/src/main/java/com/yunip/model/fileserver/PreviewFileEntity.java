/*
 * 描述：与文件服务器交互预览文件请求实体类
 * 创建人：jian.xiong
 * 创建时间：2016-5-28
 */
package com.yunip.model.fileserver;

import java.io.Serializable;

/**
 * 与文件服务器交互预览文件请求实体类
 */
public class PreviewFileEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    private String fileId;
    
    /**
     * 提取码
     */
    private String takeCode;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }
}
