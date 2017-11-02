/*
 * 描述：〈文件版本的查询类〉
 * 创建人：can.du
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.FileVersion;
import com.yunip.utils.page.PageQuery;

/**
 * 文件版本的查询类
 */
@Alias("TFileVersionQuery")
public class FileVersionQuery extends PageQuery<FileVersion> {

    private static final long serialVersionUID = 1L;

    /**文件ID**/
    private int               fileId;

    /**文件版本**/
    private int               fileVersion;
    
    /** 文件存储地址 **/
    private String            filePath;

    private boolean           desc;

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(int fileVersion) {
        this.fileVersion = fileVersion;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
