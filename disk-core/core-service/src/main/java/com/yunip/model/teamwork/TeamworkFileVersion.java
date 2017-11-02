/*
 * 描述：协作文件历史版本信息表
 * 创建人：ming.zhu
 * 创建时间：2017-02-28
 */

package com.yunip.model.teamwork;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

@Alias("TTeamworkFileVersion")
public class TeamworkFileVersion implements Serializable {
    /**主键**/
    private Integer           id;

    /**文件ID**/
    private Integer           fileId;

    /**文件名称**/
    private String            fileName;

    /**文件类型(txt,jpeg,word,excel,pdf等)**/
    private Integer           fileType;

    /**文件大小(B为单位)**/
    private Long              fileSize;

    /**文件存储地址**/
    private String            filePath;

    /**文件版本**/
    private Integer           fileVersion;

    /**员工ID**/
    private Integer           employeeId;

    /**协作id**/
    private Integer           teamworkId;

    /**所属文件夹编码**/
    private String            folderCode;

    /**有效状态（预留字段）**/
    private Integer           validStatus;

    /**创建人**/
    private String            createAdmin;

    /**创建时间**/
    private Date              createTime;

    /**更新人**/
    private String            updateAdmin;

    /**更新时间**/
    private Date              updateTime;

    /**标识文件加密状态（0:不加密,1:待加密, 2:已加密**/
    private Integer           encryptStatus;

    /**加密密钥**/
    private String            encryptKey;

    /**文件所属服务器编号**/
    private String            serverCode;

    /**********************************************************/

    /**id集合**/
    private List<String>     ids;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileType() {
        return fileType;
    }

    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(Integer fileVersion) {
        this.fileVersion = fileVersion;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getTeamworkId() {
        return teamworkId;
    }

    public void setTeamworkId(Integer teamworkId) {
        this.teamworkId = teamworkId;
    }

    public String getFolderCode() {
        return folderCode;
    }

    public void setFolderCode(String folderCode) {
        this.folderCode = folderCode;
    }

    public Integer getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Integer validStatus) {
        this.validStatus = validStatus;
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

    public Integer getEncryptStatus() {
        return encryptStatus;
    }

    public void setEncryptStatus(Integer encryptStatus) {
        this.encryptStatus = encryptStatus;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
