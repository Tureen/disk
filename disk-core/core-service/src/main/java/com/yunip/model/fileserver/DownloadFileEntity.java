/*
 * 描述：与文件服务器交互下载文件请求实体类
 * 创建人：jian.xiong
 * 创建时间：2016-5-28
 */
package com.yunip.model.fileserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 与文件服务器交互下载文件请求实体类
 */
public class DownloadFileEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 文件ID集合
     */
    private List<String> files = new ArrayList<String>();

    /**
     * 文件夹ID集合
     */
    private List<String> folders = new ArrayList<String>();
    
    /**
     * 提取码
     */
    private String takeCode;

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<String> getFolders() {
        return folders;
    }

    public void setFolders(List<String> folders) {
        this.folders = folders;
    }

    public String getTakeCode() {
        return takeCode;
    }

    public void setTakeCode(String takeCode) {
        this.takeCode = takeCode;
    }
}
