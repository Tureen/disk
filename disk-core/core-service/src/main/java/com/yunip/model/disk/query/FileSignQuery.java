/*
 * 描述：文件标签关联索引类
 * 创建人：ming.zhu
 * 创建时间：2016-5-12
 */
package com.yunip.model.disk.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import com.yunip.model.disk.FileSign;
import com.yunip.utils.page.PageQuery;

@Alias("TFileSignQuery")
public class FileSignQuery extends PageQuery<FileSign> implements Serializable {

    /**
     * 属性说明
     */
    private static final long serialVersionUID = 93769016998440634L;

    /** 分享的文件ID **/
    private Integer           fileId;

    /** 标签ID **/
    private Integer           signId;

    /** 文件名称 **/
    private String            fileName;

    /** 员工id **/
    private Integer           employeeId;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getSignId() {
        return signId;
    }

    public void setSignId(Integer signId) {
        this.signId = signId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
