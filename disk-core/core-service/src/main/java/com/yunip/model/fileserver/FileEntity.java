/*
 * 描述：与文件服务器交互的文件实体类
 * 创建人：jian.xiong
 * 创建时间：2016-5-27
 */
package com.yunip.model.fileserver;

import java.io.Serializable;

/**
 * 与文件服务器交互的文件实体类
 */
public class FileEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID
     */
    private String fileId;

    /**
     * 文件路径
     */
    private String filePath;
    
    /**
     * 文件版本ID
     */
    private String tmpId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTmpId() {
        return tmpId;
    }

    public void setTmpId(String tmpId) {
        this.tmpId = tmpId;
    }
}
