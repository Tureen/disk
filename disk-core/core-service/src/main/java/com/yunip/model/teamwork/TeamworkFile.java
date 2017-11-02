/*
 * 描述：协作文件基本信息表
 * 创建人：ming.zhu
 * 创建时间：2017-02-23
 */
package com.yunip.model.teamwork;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.utils.util.FileSizeUtil;

@Alias("TTeamworkFile")
public class TeamworkFile implements Serializable {
    /**主键**/
    private Integer           id;

    /**文件名称**/
    private String            fileName;

    /**文件类型(txt,jpeg,word,excel,pdf等)**/
    private Integer           fileType;

    /**员工id**/
    private Integer           employeeId;

    /**协作ID**/
    private Integer           teamworkId;

    /**文件大小(B为单位)**/
    private Long              fileSize;

    /**当前目录下的版本号**/
    private Integer           fileFbVersion;

    /**文件夹ID**/
    private Integer           folderId;

    /**文件夹Code**/
    private String            folderCode;

    /**文件存储地址**/
    private String            filePath;

    /**文件版本**/
    private Integer           fileVersion;

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

    /**标识文件加密状态（0:不加密,1:待加密, 2:已加密）**/
    private Integer           encryptStatus;

    /**加密密钥**/
    private String            encryptKey;

    /**文件所属服务器**/
    private String            serverCode;

    /***********************************************/

    /**文件版本**/
    private String            fileVersionNum;

    /**显示修改时间**/
    private String            updateDate;

    /**上传状态   0：新增     1：升级版本**/
    private Integer           uploadType;

    /**临时协作信息id**/
    private Integer           teamworkMessageId;
    
    /**id集合**/
    private List<String>     ids;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getFileFbVersion() {
        return fileFbVersion;
    }

    public void setFileFbVersion(Integer fileFbVersion) {
        this.fileFbVersion = fileFbVersion;
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

    public String getShowFileSize() {
        return FileSizeUtil.bytesToSize(fileSize);
    }

    public String getFileVersionNum() {
        return fileVersionNum;
    }

    public void setFileVersionNum(String fileVersionNum) {
        this.fileVersionNum = fileVersionNum;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUploadType() {
        return uploadType;
    }

    public void setUploadType(Integer uploadType) {
        this.uploadType = uploadType;
    }

    public Integer getTeamworkMessageId() {
        return teamworkMessageId;
    }

    public void setTeamworkMessageId(Integer teamworkMessageId) {
        this.teamworkMessageId = teamworkMessageId;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

}
