/*
 * 描述：员工网盘基本信息表
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.FileSizeUtil;

@Alias("TEmployeeDisk")
public class EmployeeDisk implements Serializable {
    /** 主键(员工ID) **/
    private Integer           id;

    /** 云盘使用大小(KB) **/
    private Long              diskSize;

    /** 文件夹数量 **/
    private Integer           folderNumber;

    /** 文件数量 **/
    private Integer           fileNumer;

    /** 接受的文件夹数量 **/
    private Integer           receiveFolderNumber;

    /** 接受的文件数量 **/
    private Integer           receiveFileNumber;

    /** 标签数量 **/
    private Integer           signNumber;

    /** 可提取文件数 **/
    private Integer           takeFileNumber;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Long diskSize) {
        this.diskSize = diskSize;
    }

    public Integer getFolderNumber() {
        return folderNumber;
    }

    public void setFolderNumber(Integer folderNumber) {
        this.folderNumber = folderNumber;
    }

    public Integer getFileNumer() {
        return fileNumer;
    }

    public void setFileNumer(Integer fileNumer) {
        this.fileNumer = fileNumer;
    }

    public Integer getReceiveFolderNumber() {
        return receiveFolderNumber;
    }

    public void setReceiveFolderNumber(Integer receiveFolderNumber) {
        this.receiveFolderNumber = receiveFolderNumber;
    }

    public Integer getReceiveFileNumber() {
        return receiveFileNumber;
    }

    public void setReceiveFileNumber(Integer receiveFileNumber) {
        this.receiveFileNumber = receiveFileNumber;
    }

    public Integer getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(Integer signNumber) {
        this.signNumber = signNumber;
    }

    public String getCreateAdmin() {
        return createAdmin;
    }

    public void setCreateAdmin(String createAdmin) {
        this.createAdmin = createAdmin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateAdmin() {
        return updateAdmin;
    }

    public void setUpdateAdmin(String updateAdmin) {
        this.updateAdmin = updateAdmin;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getShowDiskSize() {
        return FileSizeUtil.bytesToSize(diskSize);
    }

    public Integer getTakeFileNumber() {
        return takeFileNumber;
    }

    public void setTakeFileNumber(Integer takeFileNumber) {
        this.takeFileNumber = takeFileNumber;
    }
}
