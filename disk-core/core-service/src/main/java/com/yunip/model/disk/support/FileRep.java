/*
 * 描述：〈输出文件对象与文件服务器对接〉
 * 创建人：can.du
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.support;

import java.io.Serializable;

/**
 * 输出文件对象与文件服务器对接
 */
public class FileRep implements Serializable{

    private static final long serialVersionUID = 671098287858783076L;

    /**文件ID**/
    private int fileId;
    
    /**文件路径**/
    private String path;

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
