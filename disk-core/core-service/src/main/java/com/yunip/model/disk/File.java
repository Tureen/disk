/*
 * 描述：文件基本信息表
 * 创建人：ming.zhu
 * 创建时间：2016-5-9
 */
package com.yunip.model.disk;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.yunip.config.ClusterConfigHelper;
import com.yunip.config.SysConfigHelper;
import com.yunip.constant.SystemContant;
import com.yunip.enums.basics.BasicsBool;
import com.yunip.enums.basics.BasicsInfoCode;
import com.yunip.model.config.ClusterConfig;
import com.yunip.utils.util.FileSizeUtil;

@Alias("TFile")
public class File implements Serializable {
    /** 主键 **/
    private Integer           id;

    /** 文件名称 **/
    private String            fileName;

    /**文件后缀名**/
    private String            fileSuffix;

    /** 文件类型(txt,jpeg,word,excel,pdf等) **/
    private Integer           fileType;

    /** 员工ID **/
    private Integer           employeeId;

    /** 文件大小(B为单位) **/
    private Long              fileSize;

    /** 文件夹ID **/
    private Integer           folderId;

    /** 所属文件夹编码 **/
    private String            folderCode;

    /** 文件存储地址 **/
    private String            filePath;

    /** 文件版本 **/
    private Integer           fileVersion;

    /** 有效状态（预留字段） **/
    private Integer           validStatus;

    /** 分享状态 **/
    private Integer           shareStatus;

    /** 创建人 **/
    private String            createAdmin;

    /** 创建时间 **/
    private Date              createTime;

    /** 更新人 **/
    private String            updateAdmin;

    /** 更新时间 **/
    private Date              updateTime;

    /** 标识文件加密状态 **/
    private Integer           encryptStatus;

    /** 加密密钥 **/
    private String            encryptKey;
    
    /** 所属文件服务器编号 **/
    private String           serverCode;

    /*******************辅助字段********************/

    /** 父权限 **/
    private Integer           operAuth;

    /** 权限 **/
    private AuthorityShare    authorityShare;

    /** 对应的多个标签 **/
    private List<Sign>        signs;

    /** 对应的多个提取码 **/
    private List<TakeCode>    takecodes;

    /**显示修改时间**/
    private String            updateDate;

    /**文件内容**/
    private String            content;

    /**父类文件夹**/
    private List<Folder>      parentFolders;
    
    /**版本数**/
    private Integer           fileVersionNum;
    
    /**所属文件服务器访问地址 **/
    private String            serverUrl;

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

    public List<Sign> getSigns() {
        return signs;
    }

    public void setSigns(List<Sign> signs) {
        this.signs = signs;
    }

    public List<TakeCode> getTakecodes() {
        return takecodes;
    }

    public void setTakecodes(List<TakeCode> takecodes) {
        this.takecodes = takecodes;
    }

    public String getShowFileSize() {
        return FileSizeUtil.bytesToSize(fileSize);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Folder> getParentFolders() {
        return parentFolders;
    }

    public void setParentFolders(List<Folder> parentFolders) {
        this.parentFolders = parentFolders;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
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

    public Integer getFileVersionNum() {
        return fileVersionNum;
    }

    public void setFileVersionNum(Integer fileVersionNum) {
        this.fileVersionNum = fileVersionNum;
    }
    
    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public String getServerUrl() {
        String url = SystemContant.FILE_SERVICE_URL;
        String isOpenCluster = SysConfigHelper.getMap(SystemContant.BASICSCODE).get(BasicsInfoCode.ISOPENCLUSTER.getKey());
        if((BasicsBool.YES.getBool()).equals(isOpenCluster)){
            if(this.serverCode != null && !this.serverCode.equals("")){
                ClusterConfig config = ClusterConfigHelper.getValue(serverCode);
                if(config != null){
                    url = config.getFileUrl();
                }
            }
        }
        return url;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
