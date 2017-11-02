/*

 * 描述：文件夹信息表
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("TFolder")
public class Folder implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 文件夹编码 **/
    private String            folderCode;

    /** 文件夹名称 **/
    private String            folderName;

    /** 文件类型(1.私人,2.公共) **/
    private Integer           folderType;

    /** 有效状态（启用:1,冻结:0） **/
    private Integer           validStatus;

    /** 父文件ID **/
    private Integer           parentId;

    /** 分享状态 **/
    private Integer           shareStatus;

    /** 员工ID **/
    private Integer           employeeId;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    /******************************辅助字段*********************************/

    /** 文件夹集合（存在于父文件夹） **/
    private List<Folder>      folders;

    /** 文件集合（存在于父文件夹） **/
    private List<File>        files;

    /** 权限 **/
    private AuthorityShare    authorityShare;

    /** (最上级)父权限 **/
    private Integer           operAuth;

    /** 最上级文件夹id **/
    private Integer           openId;

    /**显示修改时间**/
    private String            updateDate;

    /**文件夹编码长度**/
    private Integer           folderCodeLength;

    private static final long serialVersionUID = 1L;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getFolderType() {
        return folderType;
    }

    public void setFolderType(Integer folderType) {
        this.folderType = folderType;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public Integer getOperAuth() {
        return operAuth;
    }

    public void setOperAuth(Integer operAuth) {
        this.operAuth = operAuth;
    }

    public Integer getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(Integer shareStatus) {
        this.shareStatus = shareStatus;
    }

    public Integer getOpenId() {
        return openId;
    }

    public void setOpenId(Integer openId) {
        this.openId = openId;
    }

    public AuthorityShare getAuthorityShare() {
        return authorityShare;
    }

    public void setAuthorityShare(AuthorityShare authorityShare) {
        this.authorityShare = authorityShare;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getFolderCodeLength() {
        return folderCodeLength;
    }

    public void setFolderCodeLength(Integer folderCodeLength) {
        this.folderCodeLength = folderCodeLength;
    }

}
