package com.yunip.model.disk.support;

public class FileEncrypt {
    
    /**
     * 文件ID
     */
    private Integer fileId;
    
    /**
     * 文件or协作文件 0：文件  1：协作文件
     */
    private Integer type;
    
    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
