/*
 * 描述：用户文件夹权限相关信息表
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.model.disk;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("TAuthorityShare")
public class AuthorityShare implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 分享的(文件夹ID或者文件) **/
    private Integer           openId;

    /** 分享的文件类型(1.文件夹,2.文件) **/
    private Integer           openType;

    /** 操作权限(具体枚举中展示) **/
    private Integer           operAuth;

    /** 被分享对象ID(部门ID或者员工ID) **/
    private String            shareId;

    /** 分享的类型(1.部门,2.员工) **/
    private Integer           shareType;

    /** 分享者员工id **/
    private Integer           employeeId;

    /** 所属文件夹编码 **/
    private String            folderCode;

    /** 父文件夹ID **/
    private Integer           folderId;

    /*****************************辅助字段********************************/

    /** 分享的文件夹对象 **/
    private Folder            folder;

    /** 分享的文件对象 **/
    private File              file;

    /**被分享的员工名**/
    private String            shareEmployeeName;

    /**被分享的部门名**/
    private String            shareDeptName;

    /**被分享的工作组**/
    private String            workgroupName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public Integer getOperAuth() {
        return operAuth;
    }

    public void setOperAuth(Integer operAuth) {
        this.operAuth = operAuth;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public Integer getShareType() {
        return shareType;
    }

    public void setShareType(Integer shareType) {
        this.shareType = shareType;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getOpenId() {
        return openId;
    }

    public void setOpenId(Integer openId) {
        this.openId = openId;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public void setFolderId(Integer folderId) {
        this.folderId = folderId;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public String getShareEmployeeName() {
        return shareEmployeeName;
    }

    public void setShareEmployeeName(String shareEmployeeName) {
        this.shareEmployeeName = shareEmployeeName;
    }

    public String getShareDeptName() {
        return shareDeptName;
    }

    public void setShareDeptName(String shareDeptName) {
        this.shareDeptName = shareDeptName;
    }

    public String getWorkgroupName() {
        return workgroupName;
    }

    public void setWorkgroupName(String workgroupName) {
        this.workgroupName = workgroupName;
    }

}
